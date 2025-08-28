package wootrevived.woot.drops.simulator;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.entity.EntityPersistentStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.woot.Woot;
import wootrevived.woot.mixin.EnderDragonMixin;
import wootrevived.woot.mixin.LivingEntityMixin;

import java.util.*;

public class DropSimulator {
    private static final DropSimulator INSTANCE = new DropSimulator();

    private final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(Woot.MOD_ID.getBytes()), Woot.MOD_ID);
    private ServerLevel dimensionLevel = null;
    private FakePlayer fakePlayer = null;
    private FakeEntityManager<Entity> fakeEntityManager = null;
    private Creeper chargedCreeper = null;
    private DamageSource playerSource = null;
    private DamageSource chargedCreeperSource = null;

    public static void simulateDrops(CompoundTag tag, WootDropsProperties properties) {
        INSTANCE.simulate(tag, properties);
    }

    public static @NotNull ServerLevel getLevel() {
        return INSTANCE.dimensionLevel;
    }

    public static @NotNull RandomSource getRandom() {
        return INSTANCE.dimensionLevel.getRandom();
    }

    public static @NotNull HolderLookup.Provider getLookupProvider(){
        return INSTANCE.dimensionLevel.registryAccess();
    }

    @SuppressWarnings({"deprecation", "OverrideOnly", "UnstableApiUsage"})
    private void simulate(CompoundTag tag, WootDropsProperties properties){
        if(dimensionLevel == null)
            return;

        Entity entity = EntityType.loadEntityRecursive(tag, dimensionLevel, e -> e);

        if(entity == null)
            return;

        if(!(entity instanceof LivingEntity livingEntity))
            return;

        if(entity instanceof Mob mob){
            var event = new MobSpawnEvent.FinalizeSpawn(mob, dimensionLevel, 0, 0, 0, dimensionLevel.getCurrentDifficultyAt(BlockPos.ZERO), MobSpawnType.SPAWNER, null, null, null);
            NeoForge.EVENT_BUS.post(event);
            mob.finalizeSpawn(dimensionLevel, dimensionLevel.getCurrentDifficultyAt(BlockPos.ZERO), MobSpawnType.SPAWNER, null, null);
        }

        ItemStack mainHand = properties.getMainHandItem();

        if(!livingEntity.fireImmune() && properties.isInFire()){
            livingEntity.setSecondsOnFire(1);
            livingEntity.setRemainingFireTicks(20);
            livingEntity.setSharedFlagOnFire(true);
        }

        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, mainHand);
        fakePlayer.setItemInHand(InteractionHand.OFF_HAND, properties.getOffHandItem());

        fakeEntityManager.clearEntityList();

        livingEntity.tickCount = 100;
        livingEntity.setLastHurtByPlayer(fakePlayer);
        RangedAttribute luckAttribute = (RangedAttribute)Attributes.LUCK;
        fakePlayer.getAttribute(luckAttribute).setBaseValue(Mth.clamp(properties.getLuck(), luckAttribute.getMinValue(), luckAttribute.getMaxValue()));

        if(livingEntity instanceof EnderDragon enderDragon){
            simulateEnderdragon(enderDragon, properties);
            return;
        }

        int i = CommonHooks.getLootingLevel(livingEntity, fakePlayer, playerSource);
        livingEntity.captureDrops(new java.util.ArrayList<>());

        LivingEntityMixin mixin = (LivingEntityMixin)livingEntity;
        mixin.invokeDropFromLootTable(playerSource, true);
        mixin.invokeDropCustomDeathLoot(playerSource, i, true);
        mixin.invokeDropEquipment();
        mixin.invokeDropExperience();

        Collection<ItemEntity> eventDrops = livingEntity.captureDrops(null);
        CommonHooks.onLivingDrops(livingEntity, playerSource, eventDrops, i, true);
        eventDrops.forEach(e -> dimensionLevel.addFreshEntity(e));

        List<ItemStack> drops = properties.getItemDrops();
        int experience = properties.getExperience();

        for(Entity droppedEntity : fakeEntityManager.getEntityList()){
            if(droppedEntity instanceof ItemEntity itemEntity){
                drops.add(itemEntity.getItem());
            } else if(droppedEntity instanceof ExperienceOrb experienceOrb){
                experience += experienceOrb.getValue();
            }
        }

        properties.setExperience(experience);

        fakeEntityManager.clearEntityList();

        if(properties.doSimulateChargedCreeper())
            drops.addAll(simulateChargedCreeper(livingEntity));
    }

    private @NotNull List<ItemStack> simulateChargedCreeper(@NotNull LivingEntity livingEntity){
        fakeEntityManager.clearEntityList();

        livingEntity.tickCount = 0;
        livingEntity.setLastHurtByPlayer(null);

        chargedCreeper.droppedSkulls = 0;

        int i = CommonHooks.getLootingLevel(livingEntity, chargedCreeper, chargedCreeperSource);
        livingEntity.captureDrops(null);

        LivingEntityMixin mixin = (LivingEntityMixin)livingEntity;
        mixin.invokeDropCustomDeathLoot(chargedCreeperSource, i, false);

        List<ItemStack> drops = new ArrayList<>();

        for(Entity droppedEntity : fakeEntityManager.getEntityList()){
            if(droppedEntity instanceof ItemEntity itemEntity){
                drops.add(itemEntity.getItem());
            }
        }

        fakeEntityManager.clearEntityList();

        return drops;
    }

    private void simulateEnderdragon(@NotNull EnderDragon enderDragon, WootDropsProperties properties){
        fakeEntityManager.clearEntityList();

        EndDragonFight.Data data = new EndDragonFight.Data(false, false, properties.isEnderDragonAlreadyKilled(), false, Optional.empty(), Optional.empty(), Optional.empty());

        enderDragon.setDragonFight(new FakeDragonFight(dimensionLevel, dimensionLevel.getSeed(), data));
        enderDragon.setSilent(true);

        EnderDragonMixin dragonMixin = (EnderDragonMixin)enderDragon;
        dragonMixin.setUnlimitedLastHurtByPlayer(fakePlayer);

        for(enderDragon.dragonDeathTime = 0; !enderDragon.getDragonFight().dragonKilled;){
            enderDragon.tickDeath();
        }

        int i = CommonHooks.getLootingLevel(enderDragon, fakePlayer, playerSource);
        enderDragon.captureDrops(new java.util.ArrayList<>());

        LivingEntityMixin mixin = (LivingEntityMixin)enderDragon;
        mixin.invokeDropFromLootTable(playerSource, true);
        mixin.invokeDropCustomDeathLoot(playerSource, i, true);
        mixin.invokeDropEquipment();
        mixin.invokeDropExperience();

        Collection<ItemEntity> eventDrops = enderDragon.captureDrops(null);
        CommonHooks.onLivingDrops(enderDragon, playerSource, eventDrops, i, true);
        eventDrops.forEach(e -> dimensionLevel.addFreshEntity(e));

        List<ItemStack> drops = properties.getItemDrops();
        int experience = properties.getExperience();

        for(Entity droppedEntity : fakeEntityManager.getEntityList()){
            if(droppedEntity instanceof ItemEntity itemEntity){
                drops.add(itemEntity.getItem());
            } else if(droppedEntity instanceof ExperienceOrb experienceOrb){
                experience += experienceOrb.getValue();
            }
        }

        properties.setExperience(experience);

        fakeEntityManager.clearEntityList();
    }

    public static void init(ServerLevel dimensionLevel){
        INSTANCE.setup(dimensionLevel);
    }

    private void setup(ServerLevel dimensionLevel){
        setDimensionLevel(dimensionLevel);
        patchDimensionLevel();
        initFakePlayer();
        initDamageSources();
    }

    private void setDimensionLevel(ServerLevel level){
        dimensionLevel = level;
    }

    private void initFakePlayer(){
        fakePlayer = new FakePlayer(dimensionLevel, gameProfile);
    }

    private void initDamageSources(){
        DamageSources sources = dimensionLevel.damageSources();

        CompoundTag creeper = new CompoundTag();
        creeper.putString("id", "minecraft:creeper");
        creeper.putBoolean("powered", true);
        chargedCreeper = (Creeper)EntityType.loadEntityRecursive(creeper, dimensionLevel, e -> e);

        playerSource = sources.playerAttack(fakePlayer);
        chargedCreeperSource = sources.explosion(chargedCreeper, chargedCreeper);
    }

    private void patchDimensionLevel(){
        EntityPersistentStorage<Entity> entityPersistentStorage = dimensionLevel.entityManager.permanentStorage;
        PersistentEntitySectionManager<Entity> persistentEntitySectionManager = dimensionLevel.entityManager;

        fakeEntityManager = new FakeEntityManager<>(Entity.class, persistentEntitySectionManager.callbacks, entityPersistentStorage);

        dimensionLevel.entityManager = fakeEntityManager;
    }
}

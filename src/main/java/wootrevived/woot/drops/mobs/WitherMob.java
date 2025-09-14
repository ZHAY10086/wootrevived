package wootrevived.woot.drops.mobs;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.woot.util.helper.EnchantmentHelper;

import java.util.List;

public class WitherMob extends WootFactoryMob<WitherBoss> {
    public WitherMob(EntityType<WitherBoss> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public void modifyDrops(Phase phase, WootDropsProperties properties){
        if(!phase.isBeforeDropCallback())
            return;

        List<ItemStack> generatedDrops = properties.getItemDrops();

        int looting = 0;
        ItemStack handStack = properties.getMainHandItem();
        if(EnchantmentHelper.isEnchanted(handStack)){
            looting = handStack.getEnchantmentLevel(Enchantments.MOB_LOOTING);
        }

        ItemStack witherRose = Items.WITHER_ROSE.getDefaultInstance();
        witherRose.setCount(1 + looting);
        generatedDrops.add(witherRose);

        if(properties.doSimulateChargedCreeper()){
            for(ItemStack stack : List.copyOf(generatedDrops)){
                if(stack.is(Items.NETHER_STAR)){
                    generatedDrops.remove(stack);
                    break;
                }
            }
        }
    }

    @Override
    public List<ItemStack> getImportItems(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        ItemStack witherSkeletonSkull = Items.WITHER_SKELETON_SKULL.getDefaultInstance();
        witherSkeletonSkull.setCount(3);

        ItemStack soulSand = Items.SOUL_SAND.getDefaultInstance();
        soulSand.setCount(4);

        return List.of(witherSkeletonSkull, soulSand);
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new WitherMob(EntityType.WITHER, new Properties().tier(Tier.TIER_5)));
    }
}

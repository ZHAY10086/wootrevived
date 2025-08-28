package wootrevived.woot.drops.mobs;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class VillagerMob extends WootFactoryMob<Villager> {
    public VillagerMob(EntityType<Villager> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        MutableComponent component = Component.empty();
        Tag villagerData = mobTag.get("VillagerData");
        DataResult<VillagerData> result = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, villagerData));
        result.result().ifPresent(data -> {
            if(data.getProfession() == VillagerProfession.NONE) return;
            component.append(Component.translatable("entity.minecraft.villager." + data.getProfession().name().toLowerCase()));
            component.append(Component.literal(" "));
        });
        component.append(super.getDisplayName(mobTag, lookupProvider));
        return component;
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        return super.getDisplayName(mobTag, lookupProvider);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        CompoundTag tag = super.saveTag(mobTag, lookupProvider);
        Tag villagerData = mobTag.get("VillagerData");
        if(villagerData != null)
            tag.put("VillagerData", villagerData);
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new VillagerMob(EntityType.VILLAGER, new Properties()));
    }
}
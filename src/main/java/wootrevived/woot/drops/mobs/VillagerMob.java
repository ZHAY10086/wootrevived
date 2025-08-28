package wootrevived.woot.drops.mobs;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
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
    public MutableComponent getDisplayName(CompoundTag mobTag) {
        MutableComponent component = Component.empty();
        Tag villagerData = mobTag.get("VillagerData");
        DataResult<VillagerData> result = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, villagerData));
        result.result().ifPresent(data -> {
            if(data.getProfession() == VillagerProfession.NONE) return;
            component.append(Component.translatable("entity.minecraft.villager." + data.getProfession().name().toLowerCase()));
            component.append(Component.literal(" "));
        });
        component.append(Component.translatable(entityType.getDescriptionId()));
        return component;
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag) {
        return super.getDisplayName(mobTag);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag){
        CompoundTag tag = super.saveTag(mobTag);
        Tag villagerData = mobTag.get("VillagerData");
        if(villagerData != null)
            tag.put("VillagerData", villagerData);
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new VillagerMob(EntityType.VILLAGER, new Properties()));
    }
}

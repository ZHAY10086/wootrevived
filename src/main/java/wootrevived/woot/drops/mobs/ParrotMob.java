package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class ParrotMob extends WootFactoryMob<Parrot> {
    public ParrotMob(EntityType<Parrot> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        Parrot.Variant variant = Parrot.Variant.byId(mobTag.getInt("Variant"));
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, variant.getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
        return tip.append(super.getDisplayName(mobTag, lookupProvider));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        return super.getDisplayName(mobTag, lookupProvider);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        CompoundTag tag = super.saveTag(mobTag, lookupProvider);
        tag.putInt("Variant", mobTag.getInt("Variant"));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new ParrotMob(EntityType.PARROT, new Properties()));
    }
}

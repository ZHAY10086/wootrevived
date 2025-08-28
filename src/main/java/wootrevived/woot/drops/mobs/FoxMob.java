package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class FoxMob extends WootFactoryMob<Fox> {
    public FoxMob(EntityType<Fox> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, mobTag.getString("Type")).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
        return tip.append(super.getDisplayName(mobTag, lookupProvider));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        return super.getDisplayName(mobTag, lookupProvider);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        CompoundTag tag = super.saveTag(mobTag, lookupProvider);
        tag.putString("Type", mobTag.getString("Type"));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new FoxMob(EntityType.FOX, new Properties()));
    }
}

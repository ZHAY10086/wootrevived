package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class FrogMob extends WootFactoryMob<Frog> {
    public FrogMob(EntityType<Frog> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        FrogVariant variant = BuiltInRegistries.FROG_VARIANT.get(ResourceLocation.tryParse(mobTag.getString("variant")));
        MutableComponent tip = Component.empty();
        if(variant != null){
            tip.append(Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, ResourceLocation.tryParse(mobTag.getString("variant")).getPath()).replaceAll("([a-z])([A-Z])", "$1 $2") + " "));
        }
        return tip.append(super.getDisplayName(mobTag, lookupProvider));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        return super.getDisplayName(mobTag, lookupProvider);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        CompoundTag tag = super.saveTag(mobTag, lookupProvider);
        tag.putString("variant", mobTag.getString("variant"));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new FrogMob(EntityType.FROG, new Properties()));
    }
}

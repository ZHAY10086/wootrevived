package wootrevived.woot.drops.mobs;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class SlimeMob extends WootFactoryMob<Slime> {
    public SlimeMob(EntityType<Slime> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        MutableComponent tip = Component.literal(
                mobTag.getInt("Size") > 0 ?
                        "Large " :
                        "Small "
        );
        return tip.append(super.getDisplayName(mobTag, lookupProvider));
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        CompoundTag tag = super.saveTag(mobTag, lookupProvider);
        tag.putInt("Size", mobTag.getInt("Size"));
        return tag;
    }

    @Override
    public boolean isSame(CompoundTag shardTag, CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        if(!super.isSame(shardTag, mobTag, lookupProvider))
            return false;

        boolean isShardLarge = shardTag.getInt("Size") > 0;
        boolean isMobLarge = mobTag.getInt("Size") > 0;
        return isShardLarge == isMobLarge;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new SlimeMob(EntityType.SLIME, new Properties().tier(Tier.TIER_2)));
    }
}

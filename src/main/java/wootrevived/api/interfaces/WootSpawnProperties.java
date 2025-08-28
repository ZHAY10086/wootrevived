package wootrevived.api.interfaces;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to the spawn properties of a mob before it is simulated
 * by a Woot factory.
 * <p>
 * This interface is passed into Woot API callbacks (e.g. upgrade items or plugins)
 * during the spawn stage. It allows inspection and modification of the mob's
 * equipment, status flags, and contextual data before the simulation begins.
 * <p>
 * Consumers can:
 * <ul>
 *   <li>Change the mob's main hand or off-hand items</li>
 *   <li>Adjust attributes such as luck or environmental flags (e.g. charged creeper, in fire, dragon killed)</li>
 *   <li>Access the factory tier, associated mob, saved NBT, server level, and random source</li>
 * </ul>
 * <p>
 * The implementation is provided by Woot; addon mods should not implement this
 * interface themselves.
 */
public interface WootSpawnProperties {
    @NotNull ItemStack getMainHandItem();
    void setMainHandItem(@NotNull ItemStack itemStack);

    @NotNull ItemStack getOffHandItem();
    void setOffHandItem(@NotNull ItemStack itemStack);

    float getLuck();
    void setLuck(float luck);

    boolean doSimulateChargedCreeper();
    void setDoSimulateChargedCreeper(boolean doSimulateChargedCreeper);

    boolean isEnderDragonAlreadyKilled();
    void setEnderDragonAlreadyKilled(boolean enderDragonAlreadyKilled);

    boolean isInFire();
    void setIsInFire(boolean isInFire);

    @NotNull ServerLevel getLevel();
    @NotNull RandomSource getRandom();
    @NotNull HolderLookup.Provider getLookupProvider();
    @NotNull Tier getFactoryTier();
    @NotNull WootFactoryMob<?> getFactoryMob();
    @NotNull CompoundTag getFactoryMobTag();
    void setFactoryMobTag(CompoundTag tag);
}

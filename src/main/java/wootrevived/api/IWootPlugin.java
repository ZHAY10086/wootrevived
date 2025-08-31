package wootrevived.api;

import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.api.registrations.WootUpgradeItemRegistration;

/**
 * Entry point for Woot plugins.
 * <p>
 * Mods integrate with Woot by implementing this interface and annotating the class
 * with {@link WootPlugin}. Woot will automatically detect and load the plugin at runtime.
 * <p>
 * All communication between an addon mod and Woot goes through this interface.
 */
public interface IWootPlugin {
    /**
     * Called during initialization to register custom upgrade items.
     *
     * @param registration the registration helper for upgrade items
     */
    default void registerUpgradeItems(WootUpgradeItemRegistration registration){
    }

    /**
     * Called during initialization to register custom factory mobs.
     *
     * @param registration the registration helper for factory mobs
     */
    default void registerFactoryMobs(WootFactoryMobRegistration registration){
    }
}

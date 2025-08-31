package wootrevived.api.registrations;

import net.minecraft.world.entity.EntityType;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.interfaces.WootDropsProperties;

import java.util.function.Consumer;

/**
 * Registration helper for adding custom factory mob behaviors and drop modifiers.
 * <p>
 * Instances of this class are passed into {@code IWootPlugin.registerFactoryMobs}
 * and should be used by plugin authors to integrate with Woot's mob simulation system.
 */
public abstract class WootFactoryMobRegistration {
    /**
     * Registers a custom {@link WootFactoryMob} implementation.
     * <p>
     * Only one instance of a given {@link WootFactoryMob} can exist in Woot's
     * internal registry. Use this when you need full control over simulation,
     * including drops, display name, shard matching, and import requirements.
     *
     * @param mob the factory mob definition to register
     */
    public abstract void registerFactoryMob(WootFactoryMob<?> mob);

    /**
     * Registers a callback to modify the drops of an existing entity type.
     * <p>
     * Use this when you only want to adjust or extend the drops of a vanilla
     * or already-registered mob, without creating a full custom {@link WootFactoryMob}.
     *
     * @param entityType the entity type whose drops should be modified
     * @param callback   a function that receives {@link WootDropsProperties} to modify drops
     */
    public abstract void registerDropsModifier(EntityType<?> entityType, Consumer<WootDropsProperties> callback);
}

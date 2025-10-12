package wootrevived.woot.compat.kubejs;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.event.*;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.woot.compat.kubejs.mobs.WootDropsPropertiesJS;
import wootrevived.woot.compat.kubejs.mobs.WootFactoryMobEventJS;

public interface WootStartupEvents {
    EventGroup GROUP = EventGroup.of("WootStartupEvents");

    Extra MOBS_EXTRA = Extra.REQUIRES_STRING.copy().validator(WootStartupEvents::validateMob);

    private static boolean validateMob(Object o) {
        try {
            ResourceLocation location = ResourceLocation.tryParse(o.toString());
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(location);
            return entityType != null;
        } catch (Exception ex) {
            return false;
        }
    }

    EventHandler MOBS = GROUP.startup("registerFactoryMob", () -> WootFactoryMobEventJS.class).extra(MOBS_EXTRA);
    EventHandler DROPS = GROUP.startup("registerGlobalDropsModifier", () -> WootDropsPropertiesJS.class);

    static void postFactoryMobs(WootFactoryMobRegistration registration) {
        registration.registerGlobalDropsModifier(WootStartupEvents::postGlobalDropsModifier);

        MOBS.forEachListener(ScriptType.STARTUP, handler -> {
            ResourceLocation location = ResourceLocation.tryParse(handler.extraId.toString());
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(location);
            WootFactoryMobEventJS event = new WootFactoryMobEventJS(registration, entityType);
            try {
                handler.handle(event, null);
            } catch (EventExit eventExit) {
                if (eventExit.result.type() == EventResult.Type.ERROR) {
                    if (DevProperties.get().debugInfo)
                        ((Throwable)eventExit.result.value()).printStackTrace();

                    ScriptType.STARTUP.console.error("Error in '" + MOBS + "'", (Throwable)eventExit.result.value());
                } else {
                    ScriptType.STARTUP.console.error("Error in '" + MOBS + "'", new IllegalStateException("Event returned result when it's not cancellable"));
                }
            }
        });
    }

    static void postGlobalDropsModifier(WootDropsProperties properties){
        DROPS.post(new WootDropsPropertiesJS(properties));
    }
}

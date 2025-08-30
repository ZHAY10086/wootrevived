package wootrevived.woot.events;

import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.Woot;
import wootrevived.woot.drops.simulator.DropSimulatorDimension;
import wootrevived.woot.drops.simulator.DropSimulator;
import wootrevived.woot.registries.WootFactoryMobsRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Woot.MOD_ID)
public class InitDropSimulator {
    public static final List<ResourceLocation> mobLocations = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerLevel level = event.getServer().getLevel(DropSimulatorDimension.DROP_SIMULATOR_LEVEL);
        if(level != null){
            DropSimulator.init(level);

            mobLocations.clear();

            for(WootFactoryMob<?> mob : List.copyOf(WootFactoryMobsRegistry.getFactoryMobValues())){
                if(mob.isBlacklisted()) continue;

                EntityType<?> entityType = mob.getEntityType();
                if(!Language.getInstance().has(entityType.getDescriptionId())) {
                    WootFactoryMobsRegistry.removeFactoryMob(entityType);
                    continue;
                }

                try {
                    Entity entity = entityType.create(level);
                    if(!(entity instanceof LivingEntity)) {
                        WootFactoryMobsRegistry.removeFactoryMob(entityType);
                        continue;
                    }

                    ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
                    mobLocations.add(location);
                } catch(Exception ignored){
                    WootFactoryMobsRegistry.removeFactoryMob(entityType);
                }
            }
        }
    }
}

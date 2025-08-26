package wootrevived.woot.events;

import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.drops.simulator.DropSimulatorDimension;
import wootrevived.woot.drops.simulator.DropSimulator;
import wootrevived.woot.registries.WootFactoryMobsRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Woot.MOD_ID)
public class InitServer {
    public static final List<ResourceLocation> mobLocations = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        RecipeManager recipeManager = event.getServer().getRecipeManager();
        StygianAnvilRecipe.loadRecipes(recipeManager);
        DyeLiquifierRecipe.loadRecipes(recipeManager);
        FluidInfuserRecipe.loadRecipes(recipeManager);
        ItemInfuserRecipe.loadRecipes(recipeManager);

        ServerLevel level = event.getServer().getLevel(DropSimulatorDimension.DROP_SIMULATOR_LEVEL);
        if(level != null){
            DropSimulator.init(level);

            mobLocations.clear();

            for(WootFactoryMob<?> mob : WootFactoryMobsRegistry.getFactoryMobValues()){
                if(mob.isBlacklisted()) continue;

                EntityType<?> entityType = mob.getEntityType();
                if(!Language.getInstance().has(entityType.getDescriptionId()))
                    continue;

                Entity entity = entityType.create(level);
                if(!(entity instanceof LivingEntity))
                    continue;

                ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
                mobLocations.add(location);
            }
        }
    }
}

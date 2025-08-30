package wootrevived.woot.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;

@Mod.EventBusSubscriber(modid = Woot.MOD_ID)
public class LoadRecipes {
    private static MinecraftServer server;

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
        loadRecipes(server.getRecipeManager());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatapackSyncEvent(OnDatapackSyncEvent event) {
        loadRecipes(server.getRecipeManager());
    }

    private static void loadRecipes(RecipeManager recipeManager) {
        StygianAnvilRecipe.loadRecipes(recipeManager);
        DyeLiquifierRecipe.loadRecipes(recipeManager);
        FluidInfuserRecipe.loadRecipes(recipeManager);
        ItemInfuserRecipe.loadRecipes(recipeManager);
    }
}

package wootrevived.woot.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;
import wootrevived.woot.Woot;
import wootrevived.woot.compat.kubejs.recipes.DyeLiquifierRecipeJS;
import wootrevived.woot.compat.kubejs.recipes.FluidInfuserRecipeJS;
import wootrevived.woot.compat.kubejs.recipes.ItemInfuserRecipeJS;
import wootrevived.woot.compat.kubejs.recipes.StygianAnvilRecipeJS;
import wootrevived.woot.registries.BlocksRegistry;

public class KubeJSWootPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.DYE_LIQUIFIER_TAG), DyeLiquifierRecipeJS.SCHEMA);
        event.register(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.FLUID_INFUSER_TAG), FluidInfuserRecipeJS.SCHEMA);
        event.register(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.ITEM_INFUSER_TAG), ItemInfuserRecipeJS.SCHEMA);
        event.register(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.STYGIAN_ANVIL_TAG), StygianAnvilRecipeJS.SCHEMA);
    }
}

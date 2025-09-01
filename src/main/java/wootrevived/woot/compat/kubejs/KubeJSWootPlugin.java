package wootrevived.woot.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.compat.kubejs.recipes.DyeLiquifierRecipeJS;
import wootrevived.woot.compat.kubejs.recipes.FluidInfuserRecipeJS;
import wootrevived.woot.compat.kubejs.recipes.ItemInfuserRecipeJS;
import wootrevived.woot.compat.kubejs.recipes.StygianAnvilRecipeJS;
import wootrevived.woot.registries.BlocksRegistry;

public class KubeJSWootPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(Woot.location(BlocksRegistry.DYE_LIQUIFIER_TAG), DyeLiquifierRecipeJS.SCHEMA);
        event.register(Woot.location(BlocksRegistry.FLUID_INFUSER_TAG), FluidInfuserRecipeJS.SCHEMA);
        event.register(Woot.location(BlocksRegistry.ITEM_INFUSER_TAG), ItemInfuserRecipeJS.SCHEMA);
        event.register(Woot.location(BlocksRegistry.STYGIAN_ANVIL_TAG), StygianAnvilRecipeJS.SCHEMA);
    }
}

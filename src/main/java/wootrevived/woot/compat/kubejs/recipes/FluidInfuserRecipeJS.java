package wootrevived.woot.compat.kubejs.recipes;

import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import wootrevived.woot.compat.kubejs.components.WootComponents;

public interface FluidInfuserRecipeJS {
    RecipeKey<OutputFluid> OUTPUT_FLUID = WootComponents.OUTPUT_FLUID.key("output_fluid");
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy");
    RecipeKey<InputFluid> INPUT_FLUID = WootComponents.INPUT_FLUID.key("input_fluid");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT_FLUID, ENERGY, INPUT_FLUID, INGREDIENT);
}

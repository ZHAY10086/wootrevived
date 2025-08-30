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
    RecipeKey<InputFluid[]> INPUT_FLUID = WootComponents.INPUT_FLUID_ARRAY.key("inputFluids");
    RecipeKey<InputItem[]> INPUT_ITEM = ItemComponents.INPUT_ARRAY.key("inputIngredients");
    RecipeKey<OutputFluid> OUTPUT_FLUID = WootComponents.OUTPUT_FLUID.key("outputFluid");
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy");

    RecipeSchema SCHEMA = new RecipeSchema(INPUT_FLUID, INPUT_ITEM, OUTPUT_FLUID, ENERGY);
}

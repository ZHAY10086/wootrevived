package wootrevived.woot.compat.kubejs.recipes;

import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import wootrevived.woot.compat.kubejs.components.WootComponents;

public interface ItemInfuserRecipeJS {
    RecipeKey<OutputItem> OUTPUT = WootComponents.OUTPUT_ITEM.key("output");
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy");
    RecipeKey<InputFluid> FLUID = WootComponents.INPUT_FLUID.key("fluid");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<InputItem> AUGMENT = ItemComponents.INPUT.key("augment").defaultOptional();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, ENERGY, FLUID, INGREDIENT, AUGMENT);
}

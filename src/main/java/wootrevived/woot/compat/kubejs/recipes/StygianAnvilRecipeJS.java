package wootrevived.woot.compat.kubejs.recipes;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import wootrevived.woot.compat.kubejs.components.WootComponents;

public interface StygianAnvilRecipeJS {
    RecipeKey<InputItem[]> INPUT_ITEM = ItemComponents.INPUT_ARRAY.key("inputIngredients");
    RecipeKey<OutputItem> OUTPUT_ITEM = WootComponents.OUTPUT_ITEM.key("outputItem");

    RecipeSchema SCHEMA = new RecipeSchema(INPUT_ITEM, OUTPUT_ITEM);
}

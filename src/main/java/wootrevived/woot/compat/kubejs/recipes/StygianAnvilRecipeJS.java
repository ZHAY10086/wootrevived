package wootrevived.woot.compat.kubejs.recipes;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import wootrevived.woot.compat.kubejs.components.WootComponents;

public interface StygianAnvilRecipeJS {
    RecipeKey<OutputItem> OUTPUT = WootComponents.OUTPUT_ITEM.key("output");
    RecipeKey<InputItem> BASE = ItemComponents.INPUT.key("base");
    RecipeKey<InputItem> FIRST_COMPLEMENTARY = ItemComponents.INPUT.key("first_complementary").defaultOptional();
    RecipeKey<InputItem> SECOND_COMPLEMENTARY = ItemComponents.INPUT.key("second_complementary").defaultOptional();
    RecipeKey<InputItem> THIRD_COMPLEMENTARY = ItemComponents.INPUT.key("third_complementary").defaultOptional();
    RecipeKey<InputItem> FOURTH_COMPLEMENTARY = ItemComponents.INPUT.key("fourth_complementary").defaultOptional();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, BASE, FIRST_COMPLEMENTARY, SECOND_COMPLEMENTARY, THIRD_COMPLEMENTARY, FOURTH_COMPLEMENTARY);
}

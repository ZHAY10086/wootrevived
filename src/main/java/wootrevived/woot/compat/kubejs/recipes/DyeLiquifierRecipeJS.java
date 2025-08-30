package wootrevived.woot.compat.kubejs.recipes;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface DyeLiquifierRecipeJS {
    RecipeKey<InputItem[]> INPUT_ITEM = ItemComponents.INPUT_ARRAY.key("inputIngredients");
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy");
    RecipeKey<Float> RED_MULTIPLIER = NumberComponent.FLOAT.key("red_multiplier");
    RecipeKey<Float> YELLOW_MULTIPLIER = NumberComponent.FLOAT.key("yellow_multiplier");
    RecipeKey<Float> BLUE_MULTIPLIER = NumberComponent.FLOAT.key("blue_multiplier");
    RecipeKey<Float> WHITE_MULTIPLIER = NumberComponent.FLOAT.key("white_multiplier");

    RecipeSchema SCHEMA = new RecipeSchema(INPUT_ITEM, ENERGY, RED_MULTIPLIER, YELLOW_MULTIPLIER, BLUE_MULTIPLIER, WHITE_MULTIPLIER);
}

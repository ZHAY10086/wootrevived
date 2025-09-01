package wootrevived.woot.recipes.dye_liquifier;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.helper.RecipeHelper;

import java.util.function.Consumer;

public class DyeLiquifierRecipeBuilder {
    private int energy;
    private float red;
    private float yellow;
    private float blue;
    private float white;
    private float multiply;
    private Ingredient ingredient;

    protected DyeLiquifierRecipeBuilder() {
        this.multiply = 1;
    }

    public static DyeLiquifierRecipeBuilder dyeLiquifierRecipe(){
        return new DyeLiquifierRecipeBuilder();
    }

    public DyeLiquifierRecipeBuilder energy(int energy){
        this.energy = energy;
        return this;
    }

    public DyeLiquifierRecipeBuilder red(float red){
        this.red = red;
        return this;
    }

    public DyeLiquifierRecipeBuilder yellow(float yellow){
        this.yellow = yellow;
        return this;
    }

    public DyeLiquifierRecipeBuilder blue(float blue){
        this.blue = blue;
        return this;
    }

    public DyeLiquifierRecipeBuilder white(float white){
        this.white = white;
        return this;
    }

    public DyeLiquifierRecipeBuilder multiply(float multiply){
        this.multiply = multiply;
        return this;
    }

    public DyeLiquifierRecipeBuilder ingredient(Ingredient ingredient){
        this.ingredient = ingredient;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, String path){
        consumer.accept(new Result(
                Woot.location(BlocksRegistry.DYE_LIQUIFIER_TAG + "/" + path),
                energy, red * multiply, yellow * multiply, blue * multiply, white * multiply, ingredient
        ));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final int energy;
        private final Ingredient ingredient;
        private final float red;
        private final float yellow;
        private final float blue;
        private final float white;

        protected Result(ResourceLocation recipeId, int energy, float red, float yellow, float blue, float white, Ingredient ingredient) {
            this.recipeId = recipeId;
            this.energy = energy;
            this.ingredient = ingredient;
            this.red = red;
            this.yellow = yellow;
            this.blue = blue;
            this.white = white;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("energy", energy);
            json.add("ingredient", RecipeHelper.IngredientInput.toJson(ingredient));
            json.addProperty("red_multiplier", this.red);
            json.addProperty("yellow_multiplier", this.yellow);
            json.addProperty("blue_multiplier", this.blue);
            json.addProperty("white_multiplier", this.white);
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipesRegistry.DYE_LIQUIFIER_RECIPE_SERIALIZER.get();
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return recipeId;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

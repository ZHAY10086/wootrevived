package wootrevived.woot.recipes.stygian_anvil;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.helper.RecipeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StygianAnvilRecipeBuilder {
    private Ingredient base;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final ItemStack output;

    protected StygianAnvilRecipeBuilder(ItemLike output, int count) {
        this.output = output.asItem().getDefaultInstance();
        this.output.setCount(count);
    }

    public static StygianAnvilRecipeBuilder anvilRecipe(ItemLike output, int count) {
        return new StygianAnvilRecipeBuilder(output, count);
    }

    public static StygianAnvilRecipeBuilder anvilRecipe(ItemLike output){
        return new StygianAnvilRecipeBuilder(output, 1);
    }

    public StygianAnvilRecipeBuilder base(Ingredient base){
        this.base = base;
        return this;
    }

    public StygianAnvilRecipeBuilder ingredient(Ingredient ingredient){
        ingredients.add(ingredient);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer){
        save(consumer, ForgeRegistries.ITEMS.getKey(output.getItem()).getPath());
    }

    public void save(Consumer<FinishedRecipe> consumer, String path){
        consumer.accept(new Result(
                ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.STYGIAN_ANVIL_TAG + "/" + path),
                base, ingredients, output
        ));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final Ingredient base;
        private final List<Ingredient> complementaries;
        private final ItemStack outputItem;

        protected Result(ResourceLocation recipeId, @NotNull Ingredient base, @NotNull List<Ingredient> complementaries, @NotNull ItemStack outputItem) {
            this.recipeId = recipeId;
            this.base = base;
            this.complementaries = complementaries;
            this.outputItem = outputItem;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("base", RecipeHelper.IngredientInput.toJson(base));

            if(!complementaries.isEmpty())
                json.add("first_complementary", RecipeHelper.IngredientInput.toJson(complementaries.get(0)));

            if(complementaries.size() > 1)
                json.add("second_complementary", RecipeHelper.IngredientInput.toJson(complementaries.get(1)));

            if(complementaries.size() > 2)
                json.add("third_complementary", RecipeHelper.IngredientInput.toJson(complementaries.get(2)));

            if(complementaries.size() > 3)
                json.add("fourth_complementary", RecipeHelper.IngredientInput.toJson(complementaries.get(3)));

            json.add("output", RecipeHelper.ItemOutput.toJson(outputItem));
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipesRegistry.ANVIL_RECIPE_SERIALIZER.get();
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

package wootrevived.woot.recipes.stygian_anvil;

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
import wootrevived.woot.util.recipes.WootFinishedRecipe;

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
        List<Ingredient> itemInputs = new ArrayList<>(1 + ingredients.size());
        itemInputs.add(base);
        itemInputs.addAll(ingredients);
        consumer.accept(new Result(
                ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.STYGIAN_ANVIL_TAG + "/" + path),
                itemInputs,
                this.output
        ));
    }

    public static class Result extends WootFinishedRecipe {
        protected Result(ResourceLocation recipeId, @Nullable List<Ingredient> itemInputs, @Nullable ItemStack output) {
            super(recipeId, -1, itemInputs, null, output, null);
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipesRegistry.ANVIL_RECIPE_SERIALIZER.get();
        }
    }
}

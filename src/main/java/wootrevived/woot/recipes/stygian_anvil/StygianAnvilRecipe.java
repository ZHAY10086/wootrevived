package wootrevived.woot.recipes.stygian_anvil;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootRecipe;

import java.util.ArrayList;
import java.util.List;

public class StygianAnvilRecipe extends WootRecipe {
    public StygianAnvilRecipe(ResourceLocation recipeId, int energy, @Nullable List<Ingredient> inputItems, @Nullable List<FluidStack> inputFluids, @Nullable ItemStack outputItem, @Nullable FluidStack outputFluid) {
        super(recipeId, energy, inputItems, inputFluids, outputItem, outputFluid);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.ANVIL_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.ANVIL_RECIPE_TYPE.get();
    }

    public Ingredient getRecipeBaseIngredient(){
        return inputItems.get(0);
    }

    public List<Ingredient> getRecipeIngredients(){
        return inputItems.subList(1, Math.min(5, inputItems.size()));
    }

    public ItemStack getOutputItem(){
        return outputItem;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        if(!getRecipeBaseIngredient().test(container.getItem(0)))
            return false;

        List<Ingredient> ingredients = getRecipeIngredients();

        int count = 0;
        for(int i = 1; i < container.getContainerSize(); i++){
            if(!container.getItem(i).isEmpty())
                count++;
        }

        if(ingredients.size() != count)
            return false;

        List<Integer> matchedSlots = new ArrayList<>();
        for(Ingredient ingredient : ingredients){
            for(int i = 1; i < container.getContainerSize(); i++){
                if(!matchedSlots.contains(i) && ingredient.test(container.getItem(i))){
                    matchedSlots.add(i);
                    break;
                }
            }
        }

        return matchedSlots.size() == ingredients.size();
    }

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        for(Recipe<?> recipe : manager.getRecipes()) {
            if(recipe instanceof StygianAnvilRecipe stygianAnvilRecipe) {
                Validator.add(stygianAnvilRecipe.getInputItems());
            }
        }
    }

    public static class Validator {
        private static final List<Ingredient> validBaseInputs = new ArrayList<>();
        private static final List<Ingredient> validIngredients = new ArrayList<>();

        public static boolean isBaseValid(ItemStack base){
            for(Ingredient ingredient : validBaseInputs){
                if(ingredient.test(base))
                    return true;
            }
            return false;
        }

        public static boolean isIngredientValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        protected static void add(List<Ingredient> items){
            validBaseInputs.add(items.get(0));
            validIngredients.addAll(items.subList(1, items.size()));
        }

        protected static void clear(){
            validBaseInputs.clear();
            validIngredients.clear();
        }
    }
}

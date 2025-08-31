package wootrevived.woot.recipes.stygian_anvil;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootRecipe;

import java.util.ArrayList;
import java.util.List;

public class StygianAnvilRecipe extends WootRecipe {
    public static final MapCodec<StygianAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("inputIngredients").forGetter(StygianAnvilRecipe::getInputItems),
            ItemStack.CODEC.fieldOf("outputItem").forGetter(StygianAnvilRecipe::getOutputItem)
    ).apply(inst, StygianAnvilRecipe::new));

    public StygianAnvilRecipe(@Nullable List<Ingredient> inputItems, @Nullable ItemStack outputItem) {
        super(0, inputItems, null, outputItem, null);
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
        for(RecipeHolder<StygianAnvilRecipe> recipeHolder : manager.getAllRecipesFor(RecipesRegistry.ANVIL_RECIPE_TYPE.get())) {
            Validator.add(recipeHolder.value().getInputItems());
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

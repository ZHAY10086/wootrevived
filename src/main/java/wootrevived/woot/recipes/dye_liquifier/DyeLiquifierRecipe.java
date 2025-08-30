package wootrevived.woot.recipes.dye_liquifier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.common.DyeMakeup;
import wootrevived.woot.util.recipes.WootRecipe;

import java.util.ArrayList;
import java.util.List;

public class DyeLiquifierRecipe extends WootRecipe {
    private final float red;
    private final float yellow;
    private final float blue;
    private final float white;

    public DyeLiquifierRecipe(ResourceLocation recipeId, int energy, float red, float yellow, float blue, float white, @Nullable List<Ingredient> inputItems) {
        super(recipeId, energy, inputItems, null, null, null);
        this.red = red;
        this.yellow = yellow;
        this.blue = blue;
        this.white = white;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.DYE_LIQUIFIER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get();
    }

    public int getRed() {
        return Math.round(red * DyeMakeup.LCM);
    }

    public int getYellow() {
        return Math.round(yellow * DyeMakeup.LCM);
    }

    public int getBlue() {
        return Math.round(blue * DyeMakeup.LCM);
    }

    public int getWhite() {
        return Math.round(white * DyeMakeup.LCM);
    }

    @Override
    public boolean matches(Container container, Level level) {
        for(Ingredient ingredient : inputItems){
            if(ingredient.test(container.getItem(0)))
                return true;
        }
        return false;
    }

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        for(Recipe<?> recipe : manager.getRecipes()) {
            if(recipe instanceof DyeLiquifierRecipe dyeLiquifierRecipe) {
                Validator.add(dyeLiquifierRecipe.getInputItems());
            }
        }
    }

    public static class Validator {
        private static final List<Ingredient> validIngredients = new ArrayList<>();

        public static boolean isIngredientValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        protected static void add(List<Ingredient> items){
            validIngredients.addAll(items);
        }

        protected static void clear(){
            validIngredients.clear();
        }
    }
}

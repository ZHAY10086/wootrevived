package wootrevived.woot.recipes.stygian_anvil;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StygianAnvilRecipe implements Recipe<WootContainer> {
    private final ResourceLocation recipeId;
    private final Ingredient base;
    private final Optional<Ingredient> firstComplementary;
    private final Optional<Ingredient> secondComplementary;
    private final Optional<Ingredient> thirdComplementary;
    private final Optional<Ingredient> fourthComplementary;
    private final ItemStack outputItem;

    private final int complementaryCount;

    public StygianAnvilRecipe(ResourceLocation recipeId, @NotNull Ingredient base, @NotNull Optional<Ingredient> firstComplementary, @NotNull Optional<Ingredient> secondComplementary, @NotNull Optional<Ingredient> thirdComplementary, @NotNull Optional<Ingredient> fourthComplementary, @NotNull ItemStack outputItem) {
        this.recipeId = recipeId;
        this.base = base;
        this.firstComplementary = firstComplementary;
        this.secondComplementary = secondComplementary;
        this.thirdComplementary = thirdComplementary;
        this.fourthComplementary = fourthComplementary;
        this.outputItem = outputItem;

        this.complementaryCount =
                Boolean.compare(firstComplementary.isPresent(), false) +
                Boolean.compare(secondComplementary.isPresent(), false) +
                Boolean.compare(thirdComplementary.isPresent(), false) +
                Boolean.compare(fourthComplementary.isPresent(), false);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.ANVIL_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.ANVIL_RECIPE_TYPE.get();
    }

    public @NotNull Ingredient getBase(){
        return base;
    }

    public @NotNull Optional<Ingredient> getFirstComplementary(){
        return firstComplementary;
    }

    public @NotNull Optional<Ingredient> getSecondComplementary(){
        return secondComplementary;
    }

    public @NotNull Optional<Ingredient> getThirdComplementary(){
        return thirdComplementary;
    }

    public @NotNull Optional<Ingredient> getFourthComplementary(){
        return fourthComplementary;
    }

    public @NotNull ItemStack getOutput(){
        return outputItem.copy();
    }

    @Override
    public boolean matches(@NotNull WootContainer container, @NotNull Level level) {
        if(!getBase().test(container.getItem(0)))
            return false;

        int count = 0;
        for(int i = 1; i < container.getContainerSize(); i++){
            if(!container.getItem(i).isEmpty())
                count++;
        }

        if(complementaryCount != count)
            return false;

        List<Integer> validatedSlots = new ArrayList<>();

        return matchComplementary(container, validatedSlots, firstComplementary) &&
                matchComplementary(container, validatedSlots, secondComplementary) &&
                matchComplementary(container, validatedSlots, thirdComplementary) &&
                matchComplementary(container, validatedSlots, fourthComplementary);
    }

    private boolean matchComplementary(Container container, List<Integer> validatedSlots, Optional<Ingredient> complementary){
        if(complementary.isEmpty())
            return true;

        boolean hasFound = false;

        for(int i = 1; i < container.getContainerSize(); i++){
            if(!validatedSlots.contains(i) && complementary.get().test(container.getItem(i))){
                validatedSlots.add(i);
                hasFound = true;
                break;
            }
        }

        return hasFound;
    }

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        for(Recipe<?> recipe : manager.getRecipes()) {
            if(recipe instanceof StygianAnvilRecipe stygianAnvilRecipe) {
                Validator.add(stygianAnvilRecipe.base, stygianAnvilRecipe.firstComplementary, stygianAnvilRecipe.secondComplementary, stygianAnvilRecipe.thirdComplementary, stygianAnvilRecipe.fourthComplementary);
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

        protected static void add(@NotNull Ingredient base, @Nullable Optional<Ingredient> firstComplementary, @Nullable Optional<Ingredient> secondComplementary, @Nullable Optional<Ingredient> thirdComplementary, @Nullable Optional<Ingredient> fourthComplementary){
            validBaseInputs.add(base);
            firstComplementary.ifPresent(validIngredients::add);
            secondComplementary.ifPresent(validIngredients::add);
            thirdComplementary.ifPresent(validIngredients::add);
            fourthComplementary.ifPresent(validIngredients::add);
        }

        protected static void clear(){
            validBaseInputs.clear();
            validIngredients.clear();
        }
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull WootContainer container, @NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return recipeId;
    }
}

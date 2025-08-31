package wootrevived.woot.recipes.item_infuser;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootContainer;

import java.util.ArrayList;
import java.util.List;

public class ItemInfuserRecipe implements Recipe<WootContainer> {
    private final ResourceLocation recipeId;
    private final int energy;
    private final FluidStack fluid;
    private final Ingredient ingredient;
    private final Ingredient augment;
    private final ItemStack output;

    public ItemInfuserRecipe(ResourceLocation recipeId, int energy, @NotNull FluidStack fluid, @NotNull Ingredient ingredient, @Nullable Ingredient augment, @NotNull ItemStack output) {
        this.recipeId = recipeId;
        this.energy = energy;
        this.fluid = fluid;
        this.ingredient = ingredient;
        this.augment = augment;
        this.output = output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.ITEM_INFUSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get();
    }

    public @NotNull FluidStack getFluid(){
        return this.fluid.copy();
    }

    public @NotNull Ingredient getIngredient(){
        return this.ingredient;
    }

    public @NotNull Ingredient getAugment(){
        if(this.augment == null)
            return Ingredient.of(ItemStack.EMPTY);
        return this.augment;
    }

    public @NotNull ItemStack getOutput(){
        return output.copy();
    }

    public int getEnergy() {
        return energy;
    }

    public int ingredientCount(Item item){
        for(ItemStack stack : ingredient.getItems()){
            if(stack.is(item))
                return stack.getCount();
        }

        return 0;
    }

    public int augmentCount(Item item){
        if(augment == null)
            return 0;

        for(ItemStack stack : augment.getItems()){
            if(stack.is(item))
                return stack.getCount();
        }

        return 0;
    }

    @Override
    public boolean matches(@NotNull WootContainer container, @NotNull Level level) {
        if(!getFluid().isFluidEqual(container.getFluid(0)))
            return false;

        if(!getIngredient().test(container.getItem(1)))
            return false;

        return getAugment().isEmpty() || getAugment().test(container.getItem(2));
    }

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        for(Recipe<?> recipe : manager.getRecipes()) {
            if(recipe instanceof ItemInfuserRecipe fluidInfuserRecipe) {
                Validator.add(fluidInfuserRecipe.ingredient, fluidInfuserRecipe.augment, fluidInfuserRecipe.fluid);
            }
        }
    }

    public static class Validator {
        private static final List<Ingredient> validIngredients = new ArrayList<>();
        private static final List<Ingredient> validAugments = new ArrayList<>();
        private static final List<FluidStack> validFluids = new ArrayList<>();

        public static boolean isIngredientValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        public static boolean isAugmentValid(ItemStack item){
            for(Ingredient ingredient : validAugments){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        public static boolean isFluidValid(FluidStack fluid){
            for(FluidStack fluidStack : validFluids){
                if(fluidStack.isFluidEqual(fluid))
                    return true;
            }
            return false;
        }

        protected static void add(Ingredient ingredient, Ingredient augment, FluidStack fluid){
            validIngredients.add(ingredient);
            validAugments.add(augment);
            validFluids.add(fluid);
        }

        protected static void clear(){
            validIngredients.clear();
            validAugments.clear();
            validFluids.clear();
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

package wootrevived.woot.recipes.fluid_infuser;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootContainer;

import java.util.ArrayList;
import java.util.List;

public class FluidInfuserRecipe implements Recipe<WootContainer> {
    private final ResourceLocation recipeId;
    private final int energy;
    private final FluidStack inputFluid;
    private final Ingredient ingredient;
    private final FluidStack outputFluid;

    public FluidInfuserRecipe(ResourceLocation recipeId, int energy, @NotNull FluidStack inputFluid, @NotNull Ingredient ingredient, @NotNull FluidStack outputFluid) {
        this.recipeId = recipeId;
        this.energy = energy;
        this.inputFluid = inputFluid;
        this.ingredient = ingredient;
        this.outputFluid = outputFluid;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.FLUID_INFUSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get();
    }

    public FluidStack getInputFluid(){
        return this.inputFluid.copy();
    }

    public Ingredient getIngredient(){
        return this.ingredient;
    }

    public FluidStack getOutputFluid(){
        return this.outputFluid.copy();
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

    @Override
    public boolean matches(@NotNull WootContainer container, @NotNull Level level) {
        if(!getInputFluid().isFluidEqual(container.getFluid(0)))
            return false;

        return getIngredient().test(container.getItem(1));
    }

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        for(Recipe<?> recipe : manager.getRecipes()) {
            if(recipe instanceof FluidInfuserRecipe fluidInfuserRecipe) {
                Validator.add(fluidInfuserRecipe.ingredient, fluidInfuserRecipe.inputFluid);
            }
        }
    }

    public static class Validator {
        private static final List<Ingredient> validIngredients = new ArrayList<>();
        private static final List<FluidStack> validFluids = new ArrayList<>();

        public static boolean isCatalystValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
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

        protected static void add(Ingredient ingredient, FluidStack fluid){
            validIngredients.add(ingredient);
            validFluids.add(fluid);
        }

        protected static void clear(){
            validIngredients.clear();
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

package wootrevived.woot.recipes.fluid_infuser;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootContainer;

import java.util.ArrayList;
import java.util.List;

public class FluidInfuserRecipe implements Recipe<WootContainer> {
    public static final MapCodec<FluidInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(FluidInfuserRecipe::getEnergy),
            FluidStack.CODEC.fieldOf("input_fluid").forGetter(FluidInfuserRecipe::getInputFluid),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(FluidInfuserRecipe::getIngredient),
            FluidStack.CODEC.fieldOf("output_fluid").forGetter(FluidInfuserRecipe::getOutputFluid)
    ).apply(inst, FluidInfuserRecipe::new));

    private final int energy;
    private final FluidStack inputFluid;
    private final Ingredient ingredient;
    private final FluidStack outputFluid;

    public FluidInfuserRecipe(int energy, @NotNull FluidStack inputFluid, @NotNull Ingredient ingredient, @NotNull FluidStack outputFluid) {
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
        for(RecipeHolder<FluidInfuserRecipe> recipeHolder : manager.getAllRecipesFor(RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get())) {
            Validator.add(recipeHolder.value().ingredient, recipeHolder.value().inputFluid);
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
}

package wootrevived.woot.recipes.item_infuser;

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
import java.util.Optional;

public class ItemInfuserRecipe implements Recipe<WootContainer> {
    public static final MapCodec<ItemInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(ItemInfuserRecipe::getEnergy),
            FluidStack.CODEC.fieldOf("fluid").forGetter(ItemInfuserRecipe::getFluid),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemInfuserRecipe::getIngredient),
            Ingredient.CODEC.optionalFieldOf("augment").forGetter(ItemInfuserRecipe::getAugment),
            ItemStack.CODEC.fieldOf("output").forGetter(ItemInfuserRecipe::getOutput)
    ).apply(inst, ItemInfuserRecipe::new));

    private final int energy;
    private final FluidStack fluid;
    private final Ingredient ingredient;
    private final Optional<Ingredient> augment;
    private final ItemStack output;

    public ItemInfuserRecipe(int energy, @NotNull FluidStack fluid, @NotNull Ingredient ingredient, @NotNull Optional<Ingredient> augment, @NotNull ItemStack output) {
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

    public @NotNull Optional<Ingredient> getAugment(){
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
        if(augment.isEmpty())
            return 0;

        for(ItemStack stack : augment.get().getItems()){
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

        return getAugment().isEmpty() || getAugment().get().test(container.getItem(2));
    }

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        for(RecipeHolder<ItemInfuserRecipe> recipeHolder : manager.getAllRecipesFor(RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get())) {
            Validator.add(recipeHolder.value().ingredient, recipeHolder.value().augment, recipeHolder.value().fluid);
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

        protected static void add(Ingredient ingredient, Optional<Ingredient> augment, FluidStack fluid){
            validIngredients.add(ingredient);
            augment.ifPresent(validAugments::add);
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
}

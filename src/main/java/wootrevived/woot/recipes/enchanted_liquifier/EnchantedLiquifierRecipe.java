package wootrevived.woot.recipes.enchanted_liquifier;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootContainer;

// Recipe class for JEI
public class EnchantedLiquifierRecipe implements Recipe<WootContainer> {
    private final int energy;
    private final Ingredient ingredient;
    private final FluidStack outputFluid;

    public EnchantedLiquifierRecipe(int energy, Ingredient ingredient, FluidStack outputFluid) {
        this.energy = energy;
        this.ingredient = ingredient;
        this.outputFluid = outputFluid;
    }

    public Ingredient getIngredient(){
        return ingredient;
    }

    public FluidStack getOutput(){
        return this.outputFluid;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        throw new IllegalStateException("Enchanted Serializer shouldn't exist");
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.ENCHANTED_LIQUIFIER_RECIPE_TYPE.get();
    }

    @Override
    public boolean matches(@NotNull WootContainer container, @NotNull Level level) {
        return false;
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
        return null;
    }
}

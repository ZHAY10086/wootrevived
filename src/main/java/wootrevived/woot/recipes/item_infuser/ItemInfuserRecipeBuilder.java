package wootrevived.woot.recipes.item_infuser;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.helper.RecipeHelper;

import java.util.function.Consumer;

public class ItemInfuserRecipeBuilder {
    private FluidStack fluid;
    private Ingredient ingredient;
    private Ingredient augment;
    private final ItemStack output;
    private int energy;

    protected ItemInfuserRecipeBuilder(ItemLike output, int count) {
        this.output = output.asItem().getDefaultInstance();
        this.output.setCount(count);
        this.augment = Ingredient.EMPTY;
    }

    public static ItemInfuserRecipeBuilder itemInfuserRecipe(ItemLike output, int count) {
        return new ItemInfuserRecipeBuilder(output, count);
    }

    public static ItemInfuserRecipeBuilder itemInfuserRecipe(ItemLike output){
        return new ItemInfuserRecipeBuilder(output, 1);
    }

    public ItemInfuserRecipeBuilder fluid(Fluid fluid, int amount){
        this.fluid = new FluidStack(fluid, amount);
        return this;
    }

    public ItemInfuserRecipeBuilder fluid(Fluid fluid){
        this.fluid = new FluidStack(fluid, 1000);
        return this;
    }

    public ItemInfuserRecipeBuilder fluid(FluidStack fluidStack){
        this.fluid = fluidStack;
        return this;
    }

    public ItemInfuserRecipeBuilder ingredient(Ingredient ingredient){
        this.ingredient = ingredient;
        return this;
    }

    public ItemInfuserRecipeBuilder augment(Ingredient augment){
        this.augment = augment;
        return this;
    }

    public ItemInfuserRecipeBuilder energy(int energy){
        this.energy = energy;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer){
        save(consumer, ForgeRegistries.ITEMS.getKey(output.getItem()).getPath());
    }

    public void save(Consumer<FinishedRecipe> consumer, String path){
        consumer.accept(new Result(
                Woot.location(BlocksRegistry.ITEM_INFUSER_TAG + "/" + path),
                energy, fluid, ingredient, augment, output
        ));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final int energy;
        private final FluidStack fluid;
        private final Ingredient ingredient;
        private final Ingredient augment;
        private final ItemStack output;

        protected Result(ResourceLocation recipeId, int energy, FluidStack fluid, Ingredient ingredient, Ingredient augment, ItemStack output) {
            this.recipeId = recipeId;
            this.energy = energy;
            this.fluid = fluid;
            this.ingredient = ingredient;
            this.augment = augment;
            this.output = output;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("energy", energy);
            json.add("fluid", RecipeHelper.FluidInput.toJson(fluid));
            json.add("ingredient", RecipeHelper.IngredientInput.toJson(ingredient));
            if(augment != null)
                json.add("augment", RecipeHelper.IngredientInput.toJson(augment));
            json.add("output", RecipeHelper.ItemOutput.toJson(output));
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipesRegistry.ITEM_INFUSER_RECIPE_SERIALIZER.get();
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return recipeId;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

package wootrevived.woot.recipes.fluid_infuser;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.util.helper.RecipeHelper;

public class FluidInfuserRecipeSerializer<T extends FluidInfuserRecipe> implements RecipeSerializer<T> {
    protected final IFactory<T> factory;

    public FluidInfuserRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull T fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        int energy = GsonHelper.getAsInt(json, "energy", 0);

        FluidStack inputFluid = RecipeHelper.FluidInput.fromJson(json, "input_fluid");
        Ingredient ingredient = RecipeHelper.IngredientInput.fromJson(json, "ingredient");
        FluidStack outputFluid = RecipeHelper.FluidOutput.fromJson(json, "output_fluid");

        return factory.create(recipeId, energy, inputFluid, ingredient, outputFluid);
    }

    @Override
    public @Nullable T fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();

        FluidStack inputFluid = RecipeHelper.FluidInput.fromNetwork(buffer);
        Ingredient ingredient = RecipeHelper.IngredientInput.fromNetwork(buffer);
        FluidStack outputFluid = RecipeHelper.FluidOutput.fromNetwork(buffer);

        return factory.create(recipeId, energy, inputFluid, ingredient, outputFluid);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeVarInt(recipe.getEnergy());

        RecipeHelper.FluidInput.toNetwork(buffer, recipe.getInputFluid());
        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getIngredient());
        RecipeHelper.FluidOutput.toNetwork(buffer, recipe.getOutputFluid());
    }

    public interface IFactory<T> {
        T create(ResourceLocation recipeId, int energy, FluidStack inputFluid, Ingredient ingredient, FluidStack outputFluid);
    }
}

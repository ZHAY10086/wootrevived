package wootrevived.woot.recipes.fluid_infuser;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.helper.RecipeHelper;

public class FluidInfuserRecipeSerializer implements RecipeSerializer<FluidInfuserRecipe> {
    protected final IFactory<FluidInfuserRecipe> factory;

    public FluidInfuserRecipeSerializer(IFactory<FluidInfuserRecipe> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull Codec<FluidInfuserRecipe> codec() {
        return FluidInfuserRecipe.CODEC.codec();
    }

    @Override
    public @NotNull FluidInfuserRecipe fromNetwork(FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();

        FluidStack inputFluid = RecipeHelper.FluidInput.fromNetwork(buffer);
        Ingredient ingredient = RecipeHelper.IngredientInput.fromNetwork(buffer);
        FluidStack outputFluid = RecipeHelper.FluidOutput.fromNetwork(buffer);

        return factory.create(energy, inputFluid, ingredient, outputFluid);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FluidInfuserRecipe recipe) {
        buffer.writeVarInt(recipe.getEnergy());

        RecipeHelper.FluidInput.toNetwork(buffer, recipe.getInputFluid());
        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getIngredient());
        RecipeHelper.FluidOutput.toNetwork(buffer, recipe.getOutputFluid());
    }

    public interface IFactory<T> {
        T create(int energy, FluidStack inputFluid, Ingredient ingredient, FluidStack outputFluid);
    }
}

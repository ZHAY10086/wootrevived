package wootrevived.woot.recipes.item_infuser;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.helper.RecipeHelper;

import java.util.Optional;

public class ItemInfuserRecipeSerializer implements RecipeSerializer<ItemInfuserRecipe> {
    protected final IFactory<ItemInfuserRecipe> factory;

    public ItemInfuserRecipeSerializer(IFactory<ItemInfuserRecipe> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull Codec<ItemInfuserRecipe> codec() {
        return ItemInfuserRecipe.CODEC.codec();
    }

    @Override
    public @NotNull ItemInfuserRecipe fromNetwork(FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();

        FluidStack fluid = RecipeHelper.FluidInput.fromNetwork(buffer);
        Ingredient ingredient = RecipeHelper.IngredientInput.fromNetwork(buffer);

        Ingredient augment = null;
        if(buffer.readBoolean())
            augment = RecipeHelper.IngredientInput.fromNetwork(buffer);

        ItemStack output = RecipeHelper.ItemOutput.fromNetwork(buffer);

        return factory.create(energy, fluid, ingredient, Optional.ofNullable(augment), output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ItemInfuserRecipe recipe) {
        buffer.writeVarInt(recipe.getEnergy());

        RecipeHelper.FluidInput.toNetwork(buffer, recipe.getFluid());
        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getIngredient());

        buffer.writeBoolean(recipe.getAugment().isPresent());
        if(recipe.getAugment().isPresent())
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getAugment().get());

        RecipeHelper.ItemOutput.toNetwork(buffer, recipe.getOutput());
    }

    public interface IFactory<T> {
        T create(int energy, FluidStack fluid, Ingredient ingredient, Optional<Ingredient> augment, ItemStack output);
    }
}

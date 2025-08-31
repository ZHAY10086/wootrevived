package wootrevived.woot.recipes.item_infuser;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.util.helper.RecipeHelper;

import java.util.Optional;

public class ItemInfuserRecipeSerializer<T extends ItemInfuserRecipe> implements RecipeSerializer<T> {
    protected final IFactory<T> factory;

    public ItemInfuserRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull T fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        int energy = GsonHelper.getAsInt(json, "energy", 0);

        FluidStack fluid = RecipeHelper.FluidInput.fromJson(json, "fluid");
        Ingredient ingredient = RecipeHelper.IngredientInput.fromJson(json, "ingredient");

        Ingredient augment = null;
        if(json.has("augment"))
            augment = RecipeHelper.IngredientInput.fromJson(json, "augment");

        ItemStack output = RecipeHelper.ItemOutput.fromJson(json, "output");

        return factory.create(recipeId, energy, fluid, ingredient, Optional.ofNullable(augment), output);
    }

    @Override
    public @Nullable T fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();

        FluidStack fluid = RecipeHelper.FluidInput.fromNetwork(buffer);
        Ingredient ingredient = RecipeHelper.IngredientInput.fromNetwork(buffer);

        Ingredient augment = null;
        if(buffer.readBoolean())
            augment = RecipeHelper.IngredientInput.fromNetwork(buffer);

        ItemStack output = RecipeHelper.ItemOutput.fromNetwork(buffer);

        return factory.create(recipeId, energy, fluid, ingredient, Optional.ofNullable(augment), output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeVarInt(recipe.getEnergy());

        RecipeHelper.FluidInput.toNetwork(buffer, recipe.getFluid());
        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getIngredient());

        buffer.writeBoolean(recipe.getAugment().isPresent());
        if(recipe.getAugment().isPresent())
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getAugment().get());

        RecipeHelper.ItemOutput.toNetwork(buffer, recipe.getOutput());
    }

    public interface IFactory<T> {
        T create(ResourceLocation recipeId, int energy, FluidStack fluid, Ingredient ingredient, Optional<Ingredient> augment, ItemStack output);
    }
}

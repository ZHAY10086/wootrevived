package wootrevived.woot.recipes.dye_liquifier;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.util.helper.RecipeHelper;

public class DyeLiquifierRecipeSerializer<T extends DyeLiquifierRecipe> implements RecipeSerializer<T> {
    protected final IFactory<T> factory;

    public DyeLiquifierRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull T fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        int energy = GsonHelper.getAsInt(json, "energy", 0);
        float red = GsonHelper.getAsFloat(json, "red_multiplier", 0);
        float yellow = GsonHelper.getAsFloat(json, "yellow_multiplier", 0);
        float blue = GsonHelper.getAsFloat(json, "blue_multiplier", 0);
        float white = GsonHelper.getAsFloat(json, "white_multiplier", 0);

        Ingredient ingredient = RecipeHelper.IngredientInput.fromJson(json, "ingredient");

        return factory.create(recipeId, energy, red, yellow, blue, white, ingredient);
    }

    @Override
    public @Nullable T fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();
        float red = buffer.readFloat();
        float yellow = buffer.readFloat();
        float blue = buffer.readFloat();
        float white = buffer.readFloat();

        Ingredient ingredient = RecipeHelper.IngredientInput.fromNetwork(buffer);

        return factory.create(recipeId, energy, red, yellow, blue, white, ingredient);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeVarInt(recipe.getEnergy());
        buffer.writeFloat(recipe.getRed());
        buffer.writeFloat(recipe.getYellow());
        buffer.writeFloat(recipe.getBlue());
        buffer.writeFloat(recipe.getWhite());

        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getInputIngredient());
    }

    public interface IFactory<T> {
        T create(ResourceLocation recipeId, int energy, float red, float yellow, float blue, float white, @NotNull Ingredient ingredient);
    }
}

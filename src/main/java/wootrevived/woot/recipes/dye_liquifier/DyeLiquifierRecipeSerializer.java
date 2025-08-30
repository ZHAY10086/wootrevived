package wootrevived.woot.recipes.dye_liquifier;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.util.recipes.WootRecipeSerializer;

import java.util.ArrayList;
import java.util.List;

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

        List<Ingredient> ingredients = WootRecipeSerializer.readInputIngredientsJson(json);

        return factory.create(recipeId, energy, red, yellow, blue, white, ingredients);
    }

    @Override
    public @Nullable T fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();
        float red = buffer.readFloat();
        float yellow = buffer.readFloat();
        float blue = buffer.readFloat();
        float white = buffer.readFloat();

        int lenInputItems = buffer.readVarInt();
        ArrayList<Ingredient> inputItems = new ArrayList<>(lenInputItems);
        for(int i = 0; i < lenInputItems; i++){
            inputItems.add(Ingredient.fromNetwork(buffer));
        }

        return factory.create(recipeId, energy, red, yellow, blue, white, inputItems);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeVarInt(recipe.getEnergy());
        buffer.writeFloat(recipe.getRed());
        buffer.writeFloat(recipe.getYellow());
        buffer.writeFloat(recipe.getBlue());
        buffer.writeFloat(recipe.getWhite());

        int lenInputItems = recipe.getInputItems().size();
        buffer.writeVarInt(lenInputItems);
        for(int i = 0; i < lenInputItems; ++i){
            recipe.getInputItems().get(i).toNetwork(buffer);
        }
    }

    public interface IFactory<T> {
        T create(ResourceLocation recipeId, int energy, float red, float yellow, float blue, float white, @Nullable List<Ingredient> inputItems);
    }
}

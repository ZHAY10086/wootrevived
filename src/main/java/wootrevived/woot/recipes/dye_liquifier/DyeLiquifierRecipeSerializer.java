package wootrevived.woot.recipes.dye_liquifier;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.helper.RecipeHelper;

public class DyeLiquifierRecipeSerializer implements RecipeSerializer<DyeLiquifierRecipe> {
    protected final IFactory<DyeLiquifierRecipe> factory;

    public DyeLiquifierRecipeSerializer(IFactory<DyeLiquifierRecipe> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull Codec<DyeLiquifierRecipe> codec() {
        return DyeLiquifierRecipe.CODEC.codec();
    }

    @Override
    public @NotNull DyeLiquifierRecipe fromNetwork(FriendlyByteBuf buffer) {
        int energy = buffer.readVarInt();
        float red = buffer.readFloat();
        float yellow = buffer.readFloat();
        float blue = buffer.readFloat();
        float white = buffer.readFloat();

        Ingredient ingredient = RecipeHelper.IngredientInput.fromNetwork(buffer);

        return factory.create(energy, red, yellow, blue, white, ingredient);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, DyeLiquifierRecipe recipe) {
        buffer.writeVarInt(recipe.getEnergy());
        buffer.writeFloat(recipe.getRed());
        buffer.writeFloat(recipe.getYellow());
        buffer.writeFloat(recipe.getBlue());
        buffer.writeFloat(recipe.getWhite());

        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getIngredient());
    }

    public interface IFactory<T> {
        T create(int energy, float red, float yellow, float blue, float white, @NotNull Ingredient ingredient);
    }
}

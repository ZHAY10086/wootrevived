package wootrevived.woot.recipes.stygian_anvil;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.util.helper.RecipeHelper;

public class StygianAnvilRecipeSerializer<T extends StygianAnvilRecipe> implements RecipeSerializer<T> {
    protected final IFactory<T> factory;

    public StygianAnvilRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull T fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        Ingredient base = RecipeHelper.IngredientInput.fromJson(json, "base");

        Ingredient firstComplementary = null;
        if(json.has("first_complementary"))
            firstComplementary = RecipeHelper.IngredientInput.fromJson(json, "first_complementary");

        Ingredient secondComplementary = null;
        if(json.has("second_complementary"))
            secondComplementary = RecipeHelper.IngredientInput.fromJson(json, "second_complementary");

        Ingredient thirdComplementary = null;
        if(json.has("third_complementary"))
            thirdComplementary = RecipeHelper.IngredientInput.fromJson(json, "third_complementary");

        Ingredient fourthComplementary = null;
        if(json.has("fourth_complementary"))
            fourthComplementary = RecipeHelper.IngredientInput.fromJson(json, "fourth_complementary");

        ItemStack outputItem = RecipeHelper.ItemOutput.fromJson(json, "output");

        return factory.create(recipeId, base, firstComplementary, secondComplementary, thirdComplementary, fourthComplementary, outputItem);
    }

    @Override
    public @Nullable T fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient base = RecipeHelper.IngredientInput.fromNetwork(buffer);

        Ingredient firstComplementary = null;
        if(buffer.readBoolean())
            firstComplementary = RecipeHelper.IngredientInput.fromNetwork(buffer);

        Ingredient secondComplementary = null;
        if(buffer.readBoolean())
            secondComplementary = RecipeHelper.IngredientInput.fromNetwork(buffer);

        Ingredient thirdComplementary = null;
        if(buffer.readBoolean())
            thirdComplementary = RecipeHelper.IngredientInput.fromNetwork(buffer);

        Ingredient fourthComplementary = null;
        if(buffer.readBoolean())
            fourthComplementary = RecipeHelper.IngredientInput.fromNetwork(buffer);

        ItemStack outputItem = RecipeHelper.ItemOutput.fromNetwork(buffer);

        return factory.create(recipeId, base, firstComplementary, secondComplementary, thirdComplementary, fourthComplementary, outputItem);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getBase());

        buffer.writeBoolean(recipe.getFirstComplementary() != null);
        if(recipe.getFirstComplementary() != null)
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getFirstComplementary());

        buffer.writeBoolean(recipe.getSecondComplementary() != null);
        if(recipe.getSecondComplementary() != null)
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getSecondComplementary());

        buffer.writeBoolean(recipe.getThirdComplementary() != null);
        if(recipe.getThirdComplementary() != null)
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getThirdComplementary());

        buffer.writeBoolean(recipe.getFourthComplementary() != null);
        if(recipe.getFourthComplementary() != null)
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getFourthComplementary());

        RecipeHelper.ItemOutput.toNetwork(buffer, recipe.getOutput());
    }

    public interface IFactory<T> {
        T create(ResourceLocation recipeId, Ingredient base, Ingredient firstComplementary, Ingredient secondComplementary, Ingredient thirdComplementary, Ingredient fourthComplementary, ItemStack outputItem);
    }
}

package wootrevived.woot.recipes.stygian_anvil;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.helper.RecipeHelper;

import java.util.Optional;

public class StygianAnvilRecipeSerializer implements RecipeSerializer<StygianAnvilRecipe> {
    protected final IFactory<StygianAnvilRecipe> factory;

    public StygianAnvilRecipeSerializer(IFactory<StygianAnvilRecipe> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull Codec<StygianAnvilRecipe> codec() {
        return StygianAnvilRecipe.CODEC.codec();
    }

    @Override
    public @NotNull StygianAnvilRecipe fromNetwork(FriendlyByteBuf buffer) {
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

        return factory.create(base, Optional.ofNullable(firstComplementary), Optional.ofNullable(secondComplementary), Optional.ofNullable(thirdComplementary), Optional.ofNullable(fourthComplementary), outputItem);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, StygianAnvilRecipe recipe) {
        RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getBase());

        buffer.writeBoolean(recipe.getFirstComplementary().isPresent());
        if(recipe.getFirstComplementary().isPresent())
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getFirstComplementary().get());

        buffer.writeBoolean(recipe.getSecondComplementary().isPresent());
        if(recipe.getSecondComplementary().isPresent())
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getSecondComplementary().get());

        buffer.writeBoolean(recipe.getThirdComplementary().isPresent());
        if(recipe.getThirdComplementary().isPresent())
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getThirdComplementary().get());

        buffer.writeBoolean(recipe.getFourthComplementary().isPresent());
        if(recipe.getFourthComplementary().isPresent())
            RecipeHelper.IngredientInput.toNetwork(buffer, recipe.getFourthComplementary().get());

        RecipeHelper.ItemOutput.toNetwork(buffer, recipe.getOutput());
    }

    public interface IFactory<T> {
        T create(Ingredient base, Optional<Ingredient> firstComplementary, Optional<Ingredient> secondComplementary, Optional<Ingredient> thirdComplementary, Optional<Ingredient> fourthComplementary, ItemStack outputItem);
    }
}

package wootrevived.woot.recipes.stygian_anvil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.ArrayList;
import java.util.List;

public class StygianAnvilRecipeBuilder {
    private Ingredient base;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final ItemStack output;

    protected StygianAnvilRecipeBuilder(ItemLike output, int count) {
        this.output = output.asItem().getDefaultInstance();
        this.output.setCount(count);
    }

    public static StygianAnvilRecipeBuilder anvilRecipe(ItemLike output, int count) {
        return new StygianAnvilRecipeBuilder(output, count);
    }

    public static StygianAnvilRecipeBuilder anvilRecipe(ItemLike output){
        return new StygianAnvilRecipeBuilder(output, 1);
    }

    public StygianAnvilRecipeBuilder base(Ingredient base){
        this.base = base;
        return this;
    }

    public StygianAnvilRecipeBuilder ingredient(Ingredient ingredient){
        ingredients.add(ingredient);
        return this;
    }

    public void save(RecipeOutput recipeOutput){
        save(recipeOutput, BuiltInRegistries.ITEM.getKey(output.getItem()).getPath());
    }

    public void save(RecipeOutput recipeOutput, String path){
        List<Ingredient> itemInputs = new ArrayList<>(1 + ingredients.size());
        itemInputs.add(base);
        itemInputs.addAll(ingredients);
        recipeOutput.accept(
                ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.STYGIAN_ANVIL_TAG + "/" + path),
                new StygianAnvilRecipe(itemInputs, output),
                null
        );
    }
}

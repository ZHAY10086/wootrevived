package wootrevived.woot.recipes.dye_liquifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.common.WootCodecs;
import wootrevived.woot.util.recipes.WootRecipe;

import java.util.ArrayList;
import java.util.List;

public class DyeLiquifierRecipe extends WootRecipe {
    public static final MapCodec<DyeLiquifierRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(DyeLiquifierRecipe::getEnergy),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("red_multiplier").forGetter(DyeLiquifierRecipe::getInternalRed),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("yellow_multiplier").forGetter(DyeLiquifierRecipe::getInternalYellow),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("blue_multiplier").forGetter(DyeLiquifierRecipe::getInternalBlue),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("white_multiplier").forGetter(DyeLiquifierRecipe::getInternalWhite),
            Ingredient.CODEC.listOf().fieldOf("inputIngredients").forGetter(DyeLiquifierRecipe::getInputItems)
    ).apply(inst, DyeLiquifierRecipe::new));

    private final float red;
    private final float yellow;
    private final float blue;
    private final float white;

    public DyeLiquifierRecipe(int energy, float red, float yellow, float blue, float white, @Nullable List<Ingredient> inputItems) {
        super(energy, inputItems, null, null, null);
        this.red = red;
        this.yellow = yellow;
        this.blue = blue;
        this.white = white;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.DYE_LIQUIFIER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get();
    }

    public int getRed() {
        return Math.round(red * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public int getYellow() {
        return Math.round(yellow * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public int getBlue() {
        return Math.round(blue * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public int getWhite() {
        return Math.round(white * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public float getInternalRed() {
        return red;
    }

    public float getInternalYellow() {
        return yellow;
    }

    public float getInternalBlue() {
        return blue;
    }

    public float getInternalWhite() {
        return white;
    }

    @Override
    public boolean matches(Container container, Level level) {
        for(Ingredient ingredient : inputItems){
            if(ingredient.test(container.getItem(0)))
                return true;
        }
        return false;
    }

    public static float maxMultiplier = 0;

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        maxMultiplier = 0;
        for(RecipeHolder<DyeLiquifierRecipe> recipeHolder : manager.getAllRecipesFor(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get())) {
            DyeLiquifierRecipe dyeLiquifierRecipe = recipeHolder.value();
            Validator.add(dyeLiquifierRecipe.getInputItems());
            if(maxMultiplier < dyeLiquifierRecipe.getInternalRed()) maxMultiplier = dyeLiquifierRecipe.getInternalRed();
            if(maxMultiplier < dyeLiquifierRecipe.getInternalYellow()) maxMultiplier = dyeLiquifierRecipe.getInternalYellow();
            if(maxMultiplier < dyeLiquifierRecipe.getInternalBlue()) maxMultiplier = dyeLiquifierRecipe.getInternalBlue();
            if(maxMultiplier < dyeLiquifierRecipe.getInternalWhite()) maxMultiplier = dyeLiquifierRecipe.getInternalWhite();
        }
    }

    public static class Validator {
        private static final List<Ingredient> validIngredients = new ArrayList<>();

        public static boolean isIngredientValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        protected static void add(List<Ingredient> items){
            validIngredients.addAll(items);
        }

        protected static void clear(){
            validIngredients.clear();
        }
    }
}

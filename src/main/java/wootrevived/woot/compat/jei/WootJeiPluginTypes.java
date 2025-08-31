package wootrevived.woot.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.BlocksRegistry;

public class WootJeiPluginTypes {
    public static final RecipeType<StygianAnvilRecipe> STYGIAN_ANVIL_TYPE = RecipeType.create(Woot.MOD_ID, BlocksRegistry.STYGIAN_ANVIL_TAG, StygianAnvilRecipe.class);
    public static final RecipeType<DyeLiquifierRecipe> DYE_LIQUIFIER_TYPE = RecipeType.create(Woot.MOD_ID, BlocksRegistry.DYE_LIQUIFIER_TAG, DyeLiquifierRecipe.class);
    public static final RecipeType<FluidInfuserRecipe> FLUID_INFUSER_TYPE = RecipeType.create(Woot.MOD_ID, BlocksRegistry.FLUID_INFUSER_TAG, FluidInfuserRecipe.class);
    public static final RecipeType<ItemInfuserRecipe> ITEM_INFUSER_TYPE = RecipeType.create(Woot.MOD_ID, BlocksRegistry.ITEM_INFUSER_TAG, ItemInfuserRecipe.class);
    public static final RecipeType<EnchantedLiquifierRecipe> ENCHANTED_LIQUIFIER_TYPE = RecipeType.create(Woot.MOD_ID, BlocksRegistry.ENCHANTED_LIQUIFIER_TAG, EnchantedLiquifierRecipe.class);
}

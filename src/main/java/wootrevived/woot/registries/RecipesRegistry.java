package wootrevived.woot.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipeSerializer;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipeSerializer;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipeSerializer;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipeSerializer;

public class RecipesRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Woot.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
    }

    /* Anvil */

    public static final DeferredHolder<RecipeType<?>, RecipeType<StygianAnvilRecipe>> ANVIL_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.STYGIAN_ANVIL_TAG, () -> RecipeType.simple(Woot.location(BlocksRegistry.STYGIAN_ANVIL_TAG)));
    public static final DeferredHolder<RecipeSerializer<?>, StygianAnvilRecipeSerializer> ANVIL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.STYGIAN_ANVIL_TAG, () -> new StygianAnvilRecipeSerializer(StygianAnvilRecipe::new));

    /* Dye Liquifier */

    public static final DeferredHolder<RecipeType<?>, RecipeType<DyeLiquifierRecipe>> DYE_LIQUIFIER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.DYE_LIQUIFIER_TAG, () -> RecipeType.simple(Woot.location(BlocksRegistry.DYE_LIQUIFIER_TAG)));
    public static final DeferredHolder<RecipeSerializer<?>, DyeLiquifierRecipeSerializer> DYE_LIQUIFIER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.DYE_LIQUIFIER_TAG, () -> new DyeLiquifierRecipeSerializer(DyeLiquifierRecipe::new));

    /* Fluid Infuser */

    public static final DeferredHolder<RecipeType<?>, RecipeType<FluidInfuserRecipe>> FLUID_INFUSER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.FLUID_INFUSER_TAG, () -> RecipeType.simple(Woot.location(BlocksRegistry.FLUID_INFUSER_TAG)));
    public static final DeferredHolder<RecipeSerializer<?>, FluidInfuserRecipeSerializer> FLUID_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.FLUID_INFUSER_TAG, () ->  new FluidInfuserRecipeSerializer(FluidInfuserRecipe::new));

    /* Item Infuser */

    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemInfuserRecipe>> ITEM_INFUSER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.ITEM_INFUSER_TAG, () -> RecipeType.simple(Woot.location(BlocksRegistry.ITEM_INFUSER_TAG)));
    public static final DeferredHolder<RecipeSerializer<?>, ItemInfuserRecipeSerializer> ITEM_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.ITEM_INFUSER_TAG, () ->  new ItemInfuserRecipeSerializer(ItemInfuserRecipe::new));

    /* Enchanted Liquifier (JEI) */

    public static final DeferredHolder<RecipeType<?>, RecipeType<EnchantedLiquifierRecipe>> ENCHANTED_LIQUIFIER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.ENCHANTED_LIQUIFIER_TAG, () -> RecipeType.simple(Woot.location(BlocksRegistry.ENCHANTED_LIQUIFIER_TAG)));
}

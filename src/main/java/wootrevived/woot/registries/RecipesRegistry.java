package wootrevived.woot.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipeSerializer;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipeSerializer;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipeSerializer;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipeSerializer;

public class RecipesRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Woot.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
    }

    /* Anvil */

    public static final RegistryObject<RecipeType<StygianAnvilRecipe>> ANVIL_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.STYGIAN_ANVIL_TAG, () -> RecipeType.simple(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.STYGIAN_ANVIL_TAG)));
    public static final RegistryObject<StygianAnvilRecipeSerializer<StygianAnvilRecipe>> ANVIL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.STYGIAN_ANVIL_TAG, () -> new StygianAnvilRecipeSerializer<>(StygianAnvilRecipe::new));

    /* Dye Liquifier */

    public static final RegistryObject<RecipeType<DyeLiquifierRecipe>> DYE_LIQUIFIER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.DYE_LIQUIFIER_TAG, () -> RecipeType.simple(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.DYE_LIQUIFIER_TAG)));
    public static final RegistryObject<DyeLiquifierRecipeSerializer<DyeLiquifierRecipe>> DYE_LIQUIFIER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.DYE_LIQUIFIER_TAG, () -> new DyeLiquifierRecipeSerializer<>(DyeLiquifierRecipe::new));

    /* Fluid Infuser */

    public static final RegistryObject<RecipeType<FluidInfuserRecipe>> FLUID_INFUSER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.FLUID_INFUSER_TAG, () -> RecipeType.simple(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.FLUID_INFUSER_TAG)));
    public static final RegistryObject<FluidInfuserRecipeSerializer<FluidInfuserRecipe>> FLUID_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.FLUID_INFUSER_TAG, () ->  new FluidInfuserRecipeSerializer<>(FluidInfuserRecipe::new));

    /* Item Infuser */

    public static final RegistryObject<RecipeType<ItemInfuserRecipe>> ITEM_INFUSER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.ITEM_INFUSER_TAG, () -> RecipeType.simple(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.ITEM_INFUSER_TAG)));
    public static final RegistryObject<ItemInfuserRecipeSerializer<ItemInfuserRecipe>> ITEM_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(BlocksRegistry.ITEM_INFUSER_TAG, () ->  new ItemInfuserRecipeSerializer<>(ItemInfuserRecipe::new));

    /* Enchanted Liquifier (JEI) */

    public static final RegistryObject<RecipeType<EnchantedLiquifierRecipe>> ENCHANTED_LIQUIFIER_RECIPE_TYPE = RECIPE_TYPES.register(BlocksRegistry.ENCHANTED_LIQUIFIER_TAG, () -> RecipeType.simple(ResourceLocation.tryBuild(Woot.MOD_ID, BlocksRegistry.ENCHANTED_LIQUIFIER_TAG)));
}

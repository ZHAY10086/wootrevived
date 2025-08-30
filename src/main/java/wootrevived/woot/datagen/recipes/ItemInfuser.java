package wootrevived.woot.datagen.recipes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import wootrevived.woot.datagen.Recipes;
import wootrevived.woot.items.dye_casing.DyeCasingItem;
import wootrevived.woot.items.dye_plate.DyePlateItem;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipeBuilder;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;

import java.util.function.Consumer;

public class ItemInfuser {
    public static void registerRecipes(Recipes recipes, Consumer<FinishedRecipe> consumer) {
        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.PRISM_ITEM.get())
                .ingredient(Ingredient.of(Tags.Items.GLASS))
                .fluid(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.SOUL_SOIL)
                .ingredient(Ingredient.of(Items.SOUL_SAND))
                .augment(Ingredient.of(Tags.Items.SAND))
                .fluid(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get(), 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.CRYING_OBSIDIAN)
                .ingredient(Ingredient.of(Tags.Items.OBSIDIAN))
                .fluid(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get(), 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.FIRE_CHARGE, 3)
                .ingredient(Ingredient.of(Items.GUNPOWDER))
                .augment(Ingredient.of(ItemTags.COALS))
                .fluid(Fluids.LAVA, 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.NETHERRACK)
                .ingredient(Ingredient.of(Items.COBBLESTONE))
                .fluid(Fluids.LAVA, 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.MAGMA_BLOCK)
                .ingredient(Ingredient.of(Items.NETHERRACK))
                .fluid(Fluids.LAVA, 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.COPPER_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 1000)
                .energy(1000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.IRON_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 2000)
                .energy(2000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.GOLD_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 3000)
                .energy(3000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.DIAMOND_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 4000)
                .energy(4000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.NETHERITE_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 5000)
                .energy(5000)
                .save(consumer);

        class Plate {
            final RegistryObject<DyeCasingItem> casing;
            final RegistryObject<DyePlateItem> plate;
            public Plate(RegistryObject<DyeCasingItem> casing, RegistryObject<DyePlateItem> plate) {
                this.casing = casing;
                this.plate = plate;
            }
        }

        Plate[] plates = {
                new Plate(ItemsRegistry.WHITE_DYE_CASING_ITEM, ItemsRegistry.WHITE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.ORANGE_DYE_CASING_ITEM, ItemsRegistry.ORANGE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.MAGENTA_DYE_CASING_ITEM, ItemsRegistry.MAGENTA_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM, ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.YELLOW_DYE_CASING_ITEM, ItemsRegistry.YELLOW_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.LIME_DYE_CASING_ITEM, ItemsRegistry.LIME_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.PINK_DYE_CASING_ITEM, ItemsRegistry.PINK_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.GRAY_DYE_CASING_ITEM, ItemsRegistry.GRAY_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM, ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.CYAN_DYE_CASING_ITEM, ItemsRegistry.CYAN_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.PURPLE_DYE_CASING_ITEM, ItemsRegistry.PURPLE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.BLUE_DYE_CASING_ITEM, ItemsRegistry.BLUE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.BROWN_DYE_CASING_ITEM, ItemsRegistry.BROWN_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.GREEN_DYE_CASING_ITEM, ItemsRegistry.GREEN_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.RED_DYE_CASING_ITEM, ItemsRegistry.RED_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.BLACK_DYE_CASING_ITEM, ItemsRegistry.BLACK_DYE_PLATE_ITEM)
        };

        for (Plate p : plates) {
            ItemInfuserRecipeBuilder.itemInfuserRecipe(p.plate.get())
                    .ingredient(Ingredient.of(p.casing.get()))
                    .fluid(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), 500)
                    .energy(500)
                    .save(consumer);
        }
    }
}

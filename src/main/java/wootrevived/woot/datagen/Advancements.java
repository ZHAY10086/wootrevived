package wootrevived.woot.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.ShardDrop;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Advancements extends ForgeAdvancementProvider {
    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    public static final class Generator implements ForgeAdvancementProvider.AdvancementGenerator {
        private static final ResourceLocation background = ResourceLocation.tryParse("textures/block/black_concrete_powder.png");

        @Override
        public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper existingFileHelper) {
            Advancement wootRevived = Advancement.Builder.advancement()
                    .display(BlocksRegistry.HEART_BLOCK.get(),
                            Component.translatable("advancements.woot_revived.root.title"),
                            Component.translatable("advancements.woot_revived.root.description"),
                            background,
                            FrameType.TASK, false, false, false)
                    .addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity())
                    .save(consumer, getNameId("root"));

            Advancement stygian_ingot = registerItem(consumer, "stygian_ingot", wootRevived, ItemsRegistry.STYGIAN_INGOT_ITEM.get());
            Advancement stygian_hammer = registerItem(consumer, "stygian_hammer", stygian_ingot, ItemsRegistry.STYGIAN_HAMMER_ITEM.get());
            Advancement stygian_anvil = registerItem(consumer, "stygian_anvil", stygian_ingot, BlocksRegistry.STYGIAN_ANVIL_BLOCK.get());

            Advancement shard_mold = registerItem(consumer, "shard_mold", stygian_anvil, ItemsRegistry.SHARD_MOLD_ITEM.get());
            Advancement dye_mold = registerItem(consumer, "dye_mold", stygian_anvil, ItemsRegistry.DYE_CASING_MOLD_ITEM.get());
            Advancement plate_mold = registerItem(consumer, "plate_mold", stygian_anvil, ItemsRegistry.PLATE_MOLD_ITEM.get());

            Advancement mob_shard = registerItem(consumer, "mob_shard", shard_mold, ItemsRegistry.MOB_SHARD_ITEM.get());
            Advancement stygian_plate = registerItem(consumer, "stygian_plate", plate_mold, ItemsRegistry.STYGIAN_PLATE_ITEM.get());

            Advancement factory_base = registerItem(consumer, "factory_base", stygian_plate, BlocksRegistry.FACTORY_BASE_BLOCK.get());

            Advancement fluid_infuser = registerItem(consumer, "fluid_infuser", factory_base, BlocksRegistry.FLUID_INFUSER_BLOCK.get());
            Advancement mob_tears_bucket = registerItem(consumer, "mob_tears_bucket", fluid_infuser, FluidsRegistry.MOB_TEARS_FLUID_BUCKET.get());
            Advancement vitality_fuel_bucket = registerItem(consumer, "vitality_fuel_bucket", mob_tears_bucket, FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET.get());

            Advancement enchanted_liquifier = registerItem(consumer, "enchanted_liquifier", factory_base, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK.get());
            Advancement enchanted_bucket = registerItem(consumer, "enchanted_bucket", enchanted_liquifier, FluidsRegistry.ENCHANTED_FLUID_BUCKET.get());

            Advancement item_infuser = registerItem(consumer, "item_infuser", enchanted_bucket, BlocksRegistry.ITEM_INFUSER_BLOCK.get());

            Advancement enchanted_copper_plate = registerItem(consumer, "enchanted_copper_plate", item_infuser, ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get());
            Advancement copper_cell = registerItem(consumer, "copper_cell", enchanted_copper_plate, BlocksRegistry.COPPER_CELL_BLOCK.get());

            Advancement enchanted_iron_plate = registerItem(consumer, "enchanted_iron_plate", item_infuser, ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get());
            Advancement iron_cell = registerItem(consumer, "iron_cell", enchanted_iron_plate, BlocksRegistry.IRON_CELL_BLOCK.get());

            Advancement enchanted_gold_plate = registerItem(consumer, "enchanted_gold_plate", item_infuser, ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get());
            Advancement gold_cell = registerItem(consumer, "gold_cell", enchanted_gold_plate, BlocksRegistry.GOLD_CELL_BLOCK.get());

            Advancement enchanted_diamond_plate = registerItem(consumer, "enchanted_diamond_plate", item_infuser, ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get());
            Advancement diamond_cell = registerItem(consumer, "diamond_cell", enchanted_diamond_plate, BlocksRegistry.DIAMOND_CELL_BLOCK.get());

            Advancement enchanted_netherite_plate = registerItem(consumer, "enchanted_netherite_plate", item_infuser, ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get());
            Advancement netherite_cell = registerItem(consumer, "netherite_cell", enchanted_netherite_plate, BlocksRegistry.NETHERITE_CELL_BLOCK.get());

            Advancement dye_liquifier = registerItem(consumer, "dye_liquifier", factory_base, BlocksRegistry.DYE_LIQUIFIER_BLOCK.get());
            Advancement pure_dye = registerItem(consumer, "pure_dye", dye_liquifier, FluidsRegistry.PURE_DYE_FLUID_BUCKET.get());

            Advancement prism = registerItem(consumer, "prism", pure_dye, ItemsRegistry.PRISM_ITEM.get());

            Advancement fake_spawner = registerItem(consumer, "fake_spawner", prism, BlocksRegistry.FAKE_SPAWNER_BLOCK.get());

            Advancement black_dye_plate = registerItem(consumer, "black_dye_plate", dye_mold, ItemsRegistry.BLACK_DYE_PLATE_ITEM.get());

            Advancement upgrade_base = registerItem(consumer, "upgrade_base", black_dye_plate, UpgradeItemsRegistry.UPGRADE_BASE_ITEM.get());

            Advancement copper_shard = registerItem(consumer, "copper_shard", shard_mold, ItemsRegistry.COPPER_SHARD_ITEM.get());
            Advancement copper_pylon = registerItem(consumer, "copper_pylon", copper_shard, BlocksRegistry.COPPER_PYLON_BLOCK.get());
            Advancement copper_plinth = registerItem(consumer, "copper_plinth", copper_shard, BlocksRegistry.COPPER_PLINTH_BLOCK.get());

            Advancement iron_shard_upgrade = registerItem(consumer, "iron_shard_upgrade", upgrade_base, ShardDrop.IRON_SHARD_DROP_ITEM.get());
            Advancement iron_shard = registerItem(consumer, "iron_shard", iron_shard_upgrade, ItemsRegistry.IRON_SHARD_ITEM.get());
            Advancement iron_pylon = registerItem(consumer, "iron_pylon", iron_shard, BlocksRegistry.IRON_PYLON_BLOCK.get());
            Advancement iron_plinth = registerItem(consumer, "iron_plinth", iron_shard, BlocksRegistry.IRON_PLINTH_BLOCK.get());

            Advancement gold_shard_upgrade = registerItem(consumer, "gold_shard_upgrade", iron_shard_upgrade, ShardDrop.GOLD_SHARD_DROP_ITEM.get());
            Advancement gold_shard = registerItem(consumer, "gold_shard", gold_shard_upgrade, ItemsRegistry.GOLD_SHARD_ITEM.get());
            Advancement gold_pylon = registerItem(consumer, "gold_pylon", gold_shard, BlocksRegistry.GOLD_PYLON_BLOCK.get());
            Advancement gold_plinth = registerItem(consumer, "gold_plinth", gold_shard, BlocksRegistry.GOLD_PLINTH_BLOCK.get());

            Advancement diamond_shard_upgrade = registerItem(consumer, "diamond_shard_upgrade", gold_shard_upgrade, ShardDrop.DIAMOND_SHARD_DROP_ITEM.get());
            Advancement diamond_shard = registerItem(consumer, "diamond_shard", diamond_shard_upgrade, ItemsRegistry.DIAMOND_SHARD_ITEM.get());
            Advancement diamond_pylon = registerItem(consumer, "diamond_pylon", diamond_shard, BlocksRegistry.DIAMOND_PYLON_BLOCK.get());
            Advancement diamond_plinth = registerItem(consumer, "diamond_plinth", diamond_shard, BlocksRegistry.DIAMOND_PLINTH_BLOCK.get());

            Advancement netherite_shard_upgrade = registerItem(consumer, "netherite_shard_upgrade", diamond_shard_upgrade, ShardDrop.NETHERITE_SHARD_DROP_ITEM.get());
            Advancement netherite_shard = registerItem(consumer, "netherite_shard", netherite_shard_upgrade, ItemsRegistry.NETHERITE_SHARD_ITEM.get());
            Advancement netherite_pylon = registerItem(consumer, "netherite_pylon", netherite_shard, BlocksRegistry.NETHERITE_PYLON_BLOCK.get());
            Advancement netherite_plinth = registerItem(consumer, "netherite_plinth", netherite_shard, BlocksRegistry.NETHERITE_PLINTH_BLOCK.get());
        }

        private Advancement registerItem(Consumer<Advancement> consumer, String name, Advancement parent, ItemLike item) {
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(
                            item.asItem(),
                            Component.translatable("advancements.woot_revived." + name + ".title"),
                            Component.translatable("advancements.woot_revived." + name + ".description"),
                            background,
                            FrameType.TASK, false, true, false
                    )
                    .addCriterion(name, InventoryChangeTrigger.TriggerInstance.hasItems(item))
                    .save(consumer, getNameId(name));
        }

        private String getNameId(String id) {
            return Woot.MOD_ID + ":main/" + id;
        }
    }
}

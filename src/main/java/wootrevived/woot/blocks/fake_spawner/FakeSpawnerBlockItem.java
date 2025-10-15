package wootrevived.woot.blocks.fake_spawner;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.blocks.factory.FactoryBlockItem;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.common.WootTier;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.helper.ModNameHelper;

import java.util.List;

import static wootrevived.woot.util.render.WootStyles.*;

public class FakeSpawnerBlockItem extends FactoryBlockItem {
    public FakeSpawnerBlockItem(Block block, Item.Properties properties) { super(block, properties); }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if(tag != null && tag.contains(WootTags.MOB_TAG)) {
            CompoundTag mobTag = tag.getCompound(WootTags.MOB_TAG);

            if(level == null)
                level = Minecraft.getInstance().level;

            WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(mobTag);
            if(mob != null) {
                tooltip.add(mob.getDisplayName(mobTag, level.registryAccess()).setStyle(CAPTURED_STYLE));
                String modId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getEntityType()).getNamespace();
                tooltip.add(ModNameHelper.getModName(modId).setStyle(MOD_NAME_STYLE));
            }

            tooltip.add(Component.translatable("info.woot_revived.tier").append(Component.literal(": ")).append(Component.translatable(WootTier.getTranslationKey(mob.getTier()))).setStyle(DESCRIPTION_STYLE));
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level level, @Nullable Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        if(stack.hasTag() && stack.getTagElement("BlockEntityTag") != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof FakeSpawnerBlockEntity fakeSpawnerBlockEntity) {
                CompoundTag tag = stack.getTagElement("BlockEntityTag");
                fakeSpawnerBlockEntity.load(tag);
                fakeSpawnerBlockEntity.setChanged();
            }
        }

        return true;
    }
}

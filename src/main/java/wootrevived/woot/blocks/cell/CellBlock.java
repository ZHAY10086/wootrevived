package wootrevived.woot.blocks.cell;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.config.CellConfig;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.block.FactoryBlockBase;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.List;
import java.util.function.Supplier;

import static wootrevived.woot.util.render.WootStyles.MACHINE_STYLE;
import static wootrevived.woot.util.render.WootStyles.UNIT_STYLE;

public class CellBlock extends FactoryBlockBase {
    public CellBlock(Supplier<BlockEntityType<?>> entity) {
        super(entity, Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.cellStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any()
                .setValue(BlockStateProperties.ATTACHED, false)
                .setValue(BlockStateProperties.ENABLED, true));
    }

    protected StateDefinition<Block, BlockState> cellStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.cellStateDefinition;
    }

    @Override
    public boolean verifyItem(Item item){
        return item == BlocksRegistry.COPPER_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.IRON_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.GOLD_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.DIAMOND_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.NETHERITE_CELL_BLOCK_ITEM.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter block, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, block, tooltip, flag);
        int capacity = 0;
        if(entity.get() == BlocksRegistry.COPPER_CELL_BLOCK_ENTITY.get()) {
            capacity = CellConfig.COPPER_CAPACITY.get();
        } else if(entity.get() == BlocksRegistry.IRON_CELL_BLOCK_ENTITY.get()) {
            capacity = CellConfig.IRON_CAPACITY.get();
        } else if(entity.get() == BlocksRegistry.GOLD_CELL_BLOCK_ENTITY.get()) {
            capacity = CellConfig.GOLD_CAPACITY.get();
        } else if(entity.get() == BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY.get()) {
            capacity = CellConfig.DIAMOND_CAPACITY.get();
        } else if(entity.get() == BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY.get()) {
            capacity = CellConfig.NETHERITE_CAPACITY.get();
        }

        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if(tag != null && tag.contains(WootTags.INPUT_TANK_TAG)){
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound(WootTags.INPUT_TANK_TAG));
            tooltip.add(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.cell.amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(fluid.getAmount()))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(capacity))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
            );
        }

    }

    public static class State extends FactoryBlockBase.State {
        public State(Block block, ImmutableMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            if(level.isClientSide)
                return InteractionResult.SUCCESS;

            if(!getValue(BlockStateProperties.ENABLED))
                return InteractionResult.PASS;

            if(!(level.getBlockEntity(hit.getBlockPos()) instanceof CellBlockEntity))
                throw new IllegalStateException("BlockEntity is missing");

            ItemStack heldItem = player.getItemInHand(hand);
            if(FluidUtil.getFluidHandler(heldItem).isPresent())
                return FluidUtil.interactWithFluidHandler(player, hand, level, hit.getBlockPos(), hit.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;

            return super.use(level, player, hand, hit);
        }
    }
}

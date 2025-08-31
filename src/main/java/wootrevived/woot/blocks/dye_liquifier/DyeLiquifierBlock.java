package wootrevived.woot.blocks.dye_liquifier;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.List;

import static wootrevived.woot.util.render.WootStyles.MACHINE_STYLE;
import static wootrevived.woot.util.render.WootStyles.UNIT_STYLE;

public class DyeLiquifierBlock extends Block implements EntityBlock {
    public DyeLiquifierBlock() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.dyeLiquifierStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    protected StateDefinition<Block, BlockState> dyeLiquifierStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.dyeLiquifierStateDefinition;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlocksRegistry.DYE_LIQUIFIER_BLOCK_ENTITY.get().create(pos, state);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return blockEntityType == BlocksRegistry.DYE_LIQUIFIER_BLOCK_ENTITY.get() ? DyeLiquifierBlockEntity::ticker : null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter block, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, block, tooltip, flag);

        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if (tag == null)
            return;

        if (tag.contains(WootTags.ENERGY_TAG)) {
            CompoundTag energyTag = tag.getCompound(WootTags.ENERGY_TAG);
            tooltip.add(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.power").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(Component.literal(WootContainerScreen.formatInteger(energyTag.getInt(WootTags.ENERGY_TAG))))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(DyeLiquifierConfig.ENERGY_CAPACITY.get()))
                            .append(Component.literal(" FE").setStyle(UNIT_STYLE))
            );
        }

        if (tag.contains(WootTags.DyeLiquifier.INTERNAL_DYE_TANKS_TAG)) {
            CompoundTag nbtDye = tag.getCompound(WootTags.DyeLiquifier.INTERNAL_DYE_TANKS_TAG);
            appendColor(tooltip, nbtDye.getInt(WootTags.DyeLiquifier.RED_TAG), DyeLiquifierConfig.RED_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.red"));
            appendColor(tooltip, nbtDye.getInt(WootTags.DyeLiquifier.YELLOW_TAG), DyeLiquifierConfig.YELLOW_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.yellow"));
            appendColor(tooltip, nbtDye.getInt(WootTags.DyeLiquifier.BLUE_TAG), DyeLiquifierConfig.BLUE_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.blue"));
            appendColor(tooltip, nbtDye.getInt(WootTags.DyeLiquifier.WHITE_TAG), DyeLiquifierConfig.WHITE_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.white"));
        }

        if (tag.contains(WootTags.OUTPUT_TANK_TAG)) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound(WootTags.OUTPUT_TANK_TAG));
            tooltip.add(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.output_fluid").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(fluid != null && !fluid.isEmpty() ? fluid.getDisplayName() : Component.translatable("info.woot_revived.empty"))
            );

            tooltip.add(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.output_amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(fluid.getAmount()))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(DyeLiquifierConfig.OUTPUT_TANK_CAPACITY.get()))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
            );
        }
    }

    private void appendColor(List<Component> tooltip, int amount, int capacity, MutableComponent colorName){
        tooltip.add(
            Component.empty()
                    .append(colorName.append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                    .append(Component.literal(WootContainerScreen.formatInteger(amount)))
                        .append(Component.literal("/").setStyle(MACHINE_STYLE))
                        .append(WootContainerScreen.formatInteger(capacity))
                        .append(Component.literal("mB").setStyle(UNIT_STYLE))
        );
    }

    public static class State extends BlockState {
        public State(Block block, ImmutableMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            if (level.isClientSide)
                return InteractionResult.SUCCESS;

            if (!(level.getBlockEntity(hit.getBlockPos()) instanceof DyeLiquifierBlockEntity liquifier))
                throw new IllegalStateException("BlockEntity is missing");

            ItemStack heldItem = player.getItemInHand(hand);

            if (FluidUtil.getFluidHandler(heldItem).isPresent())
                return FluidUtil.interactWithFluidHandler(player, hand, level, hit.getBlockPos(), hit.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;

            NetworkHooks.openScreen((ServerPlayer) player, liquifier, hit.getBlockPos());

            return InteractionResult.SUCCESS;
        }

        @Override
        public void onRemove(@NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
            if (getBlock() != newState.getBlock()) {
                BlockEntity te = level.getBlockEntity(pos);
                if (te instanceof DyeLiquifierBlockEntity liquifier)
                    liquifier.dropContents(level, pos);
                super.onRemove( level, pos, newState, isMoving);
            }
        }

        public BlockState rotate(LevelAccessor level, BlockPos pos, Rotation rotation) {
            return setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }

        @Override
        public @NotNull BlockState mirror(Mirror mirror) {
            return rotate(null, null, mirror.getRotation(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }
    }
}
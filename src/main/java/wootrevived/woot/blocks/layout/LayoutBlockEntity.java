package wootrevived.woot.blocks.layout;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.enums.Tier;
import wootrevived.woot.multiblock.patterns.Pattern;
import wootrevived.woot.multiblock.patterns.Patterns;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.block.FactoryBlockBase;
import wootrevived.woot.util.common.WootTier;

import java.util.Arrays;

public class LayoutBlockEntity extends BlockEntity implements BlockEntityTicker<BlockEntity> {
    public LayoutBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.LAYOUT_BLOCK_ENTITY.get(), pos, state);
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof LayoutBlockEntity layoutBlockEntity){
            layoutBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    private int blockRenderOffset = 0;

    public int getBlockRenderOffset() {
        return blockRenderOffset;
    }

    private static final int TICK_DELAY = 20;
    private int delayTick = TICK_DELAY;

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        int height = Patterns.getHeight();

        BlockPos layoutPos = switch(facing){
            case NORTH -> getBlockPos().offset(0, height, -1);
            case SOUTH -> getBlockPos().offset(0, height, 1);
            case EAST -> getBlockPos().offset(1, height, 0);
            case WEST -> getBlockPos().offset(-1, height, 0);
            default -> getBlockPos();
        };

        for(Pattern.PatternBlock patternBlock : Patterns.TIER_1.patterns.get(facing))
            if(Tier.TIER_1.isFactoryTierValid(tier)) placePatternBlock(level, facing, layoutPos, patternBlock, getBlockRenderOffset());
            else removePatternBlock(level, layoutPos, patternBlock);

        for(Pattern.PatternBlock patternBlock : Patterns.TIER_2.patterns.get(facing))
            if(Tier.TIER_2.isFactoryTierValid(tier)) placePatternBlock(level, facing, layoutPos, patternBlock, getBlockRenderOffset());
            else removePatternBlock(level, layoutPos, patternBlock);

        for(Pattern.PatternBlock patternBlock : Patterns.TIER_3.patterns.get(facing))
            if(Tier.TIER_3.isFactoryTierValid(tier)) placePatternBlock(level, facing, layoutPos, patternBlock, getBlockRenderOffset());
            else removePatternBlock(level, layoutPos, patternBlock);

        for(Pattern.PatternBlock patternBlock : Patterns.TIER_4.patterns.get(facing))
            if(Tier.TIER_4.isFactoryTierValid(tier)) placePatternBlock(level, facing, layoutPos, patternBlock, getBlockRenderOffset());
            else removePatternBlock(level, layoutPos, patternBlock);

        for(Pattern.PatternBlock patternBlock : Patterns.TIER_5.patterns.get(facing))
            if(Tier.TIER_5.isFactoryTierValid(tier)) placePatternBlock(level, facing, layoutPos, patternBlock, getBlockRenderOffset());
            else removePatternBlock(level, layoutPos, patternBlock);

        if(delayTick-- != 0)
            return;

        delayTick = TICK_DELAY;

        blockRenderOffset++;
    }



    public static void placePatternBlock(@NotNull Level level, Direction facing, BlockPos layoutPos, Pattern.PatternBlock patternBlock, int blockRenderOffset) {
        Block block = patternBlock.blocks[blockRenderOffset % (int) Arrays.stream(patternBlock.blocks).count()];
        BlockState blockState = block.getStateDefinition().any();
        if(blockState.hasProperty(BlockStateProperties.ENABLED))
            blockState = blockState.setValue(BlockStateProperties.ENABLED, false);
        if(blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            blockState = blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);

        BlockPos blockPos = patternBlock.getLevelBlockPos(layoutPos);

        BlockState state = level.getBlockState(blockPos);
        if(state.getBlock() == Blocks.AIR)
            level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
        else {
            if(state.getBlock() instanceof FactoryBlockBase && state.getBlock() != block && !state.getValue(BlockStateProperties.ENABLED))
                level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
        }
    }

    public static void removePatternBlock(@NotNull Level level, BlockPos layoutPos, Pattern.PatternBlock patternBlock) {
        for(Block block :  patternBlock.blocks){
            BlockPos blockPos = patternBlock.getLevelBlockPos(layoutPos);

            BlockState s = level.getBlockState(blockPos);
            if(s.getBlock() instanceof FactoryBlockBase && s.getBlock() == block && !s.getValue(BlockStateProperties.ENABLED))
                level.removeBlock(blockPos, false);
        }
    }

    private Tier tier = Tier.TIER_1;

    public Tier getTier(){
        return tier;
    }

    public void setNextTier(){
        tier = WootTier.getNextValid(tier);
        setChanged();
    }

    static final String KEY_TIER = "tier";

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(KEY_TIER, tier.ordinal());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if(tag.contains(KEY_TIER))
            tier = WootTier.byIndex(tag.getInt(KEY_TIER));
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag){
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(this.level == null || this.level.isClientSide) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}

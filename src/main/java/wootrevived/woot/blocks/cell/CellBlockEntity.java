package wootrevived.woot.blocks.cell;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.config.CellConfig;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidTankHandler;

public class CellBlockEntity extends FactoryBlockBaseEntity {
    public CellBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
        tankHandler.setCapacity(getCapacity());
    }

    public final WootFluidTankHandler tankHandler = createTank();

    private WootFluidTankHandler createTank() {
        return new WootFluidTankHandler(1000, false, (stack) -> stack.isFluidEqual(new FluidStack(FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID.get(), 1))) {
            @Override
            protected void onContentsChanged() {
                setChanged();
            }
        };
    }

    private int getCapacity(){
        if(getType() == BlocksRegistry.COPPER_CELL_BLOCK_ENTITY.get())
            return CellConfig.COPPER_CAPACITY.get();
        if(getType() == BlocksRegistry.IRON_CELL_BLOCK_ENTITY.get())
            return CellConfig.IRON_CAPACITY.get();
        if(getType() == BlocksRegistry.GOLD_CELL_BLOCK_ENTITY.get())
            return CellConfig.GOLD_CAPACITY.get();
        if(getType() == BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY.get())
            return CellConfig.DIAMOND_CAPACITY.get();
        if(getType() == BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY.get())
            return CellConfig.NETHERITE_CAPACITY.get();
        return 0;
    }

    public static IFluidHandler getFluidHandlerCapability(CellBlockEntity blockEntity, Direction side){
        return blockEntity.tankHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        tag.put(WootTags.INPUT_TANK_TAG, tankHandler.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        tankHandler.readFromNBT(tag.getCompound(WootTags.INPUT_TANK_TAG));
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
    public void setChanged() {
        super.setChanged();

        if(this.level == null || this.level.isClientSide) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}

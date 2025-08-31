package wootrevived.woot.blocks.creative_tank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidTankHandler;

public class CreativeTankBlockEntity extends BlockEntity implements BlockEntityTicker<BlockEntity> {
    public CreativeTankBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.CREATIVE_TANK_BLOCK_ENTITY.get(), pos, state);
    }

    public WootFluidTankHandler inputTankHandler = createInputTank();
    public LazyOptional<WootFluidTankHandler> inputTank = LazyOptional.of(() -> inputTankHandler);

    private WootFluidTankHandler createInputTank() {
        return new WootFluidTankHandler(Integer.MAX_VALUE, false) {
            @Override
            protected void onContentsChanged() {
                if(!this.getFluid().isEmpty()) this.getFluid().setAmount(Integer.MAX_VALUE);
                setChanged();
            }
        };
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof CreativeTankBlockEntity creativeTankBlockEntity){
            creativeTankBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        for (Direction facing : Direction.values()) {
            BlockEntity be = level.getBlockEntity(getBlockPos().relative(facing));

            if(be == null)
                continue;

            be.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent((tank) -> tank.fill(new FluidStack(this.inputTankHandler.getFluid().getFluid(), tank.getTankCapacity(0)), IFluidHandler.FluidAction.EXECUTE));
        }
    }

    public void setMaxCapacity(){ inputTankHandler.getFluid().setAmount(Integer.MAX_VALUE); setChanged(); }

    public void emptyIfDifferentFluidStack(FluidStack fluidStack){
        if(fluidStack.isEmpty())
            return;

        if(!inputTankHandler.getFluid().isFluidEqual(fluidStack))
            inputTankHandler.setFluid(FluidStack.EMPTY);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return inputTank.cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        tag.put(WootTags.INPUT_TANK_TAG, inputTankHandler.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        inputTankHandler.readFromNBT(tag.getCompound(WootTags.INPUT_TANK_TAG));
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

    @org.jetbrains.annotations.Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(this.level == null || this.level.isClientSide) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}

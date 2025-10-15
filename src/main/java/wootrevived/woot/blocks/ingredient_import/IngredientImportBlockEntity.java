package wootrevived.woot.blocks.ingredient_import;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.handlers.WootImportFluidHandler;
import wootrevived.woot.util.handlers.WootImportItemHandler;

import java.util.List;

public class IngredientImportBlockEntity extends FactoryBlockBaseEntity {
    public IngredientImportBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.IMPORT_BLOCK_ENTITY.get(), pos, state);
    }

    private final WootImportItemHandler itemHandler = new WootImportItemHandler();
    private final LazyOptional<IItemHandler> item = LazyOptional.of(() -> itemHandler);
    private final WootImportFluidHandler fluidHandler = new WootImportFluidHandler();
    private final LazyOptional<IFluidHandler> fluid = LazyOptional.of(() -> fluidHandler);

    public void setImportItem(int index, List<ItemStack> importItem){
        itemHandler.setImportItem(index, importItem);
    }

    public void setImportFluid(int index, List<FluidStack> importFluid){
        fluidHandler.setImportFluid(index, importFluid);
    }

    public boolean isImportValid(int index){
        return itemHandler.isImportValid(index) && fluidHandler.isImportValid(index);
    }

    public void consumeImports(int index){
        itemHandler.consume(index);
        fluidHandler.consume(index);
    }

    public void extractNeighbors(){
        for(Direction direction : Direction.values()){
            if(direction == Direction.UP || direction == Direction.DOWN) continue;

            BlockPos blockPos = getBlockPos().relative(direction);

            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity == null) continue;

            entity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).ifPresent(handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    ItemStack result = itemHandler.insertItem(i, stack, true);
                    if(result.getCount() < stack.getCount()){
                        ItemStack extracted = handler.extractItem(i, stack.getCount() - result.getCount(), false);
                        if(!extracted.isEmpty())
                            itemHandler.insertItem(i, extracted, false);
                    }
                }
            });

            entity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).ifPresent(handler -> {
                for (int i = 0; i < handler.getTanks(); i++) {
                    FluidStack stack = handler.getFluidInTank(i);
                    int filled = fluidHandler.fill(stack, IFluidHandler.FluidAction.SIMULATE);
                    if(filled > 0){
                        FluidStack drained = handler.drain(new FluidStack(stack.getFluid(), filled), IFluidHandler.FluidAction.EXECUTE);
                        if(!drained.isEmpty())
                            fluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            });
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side){
        if(!getBlockState().getValue(BlockStateProperties.ENABLED))
            return LazyOptional.empty();

        if(ForgeCapabilities.FLUID_HANDLER.equals(cap)){
            return fluid.cast();
        } else if(ForgeCapabilities.ITEM_HANDLER.equals(cap)){
            return item.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        itemHandler.save(tag);
        fluidHandler.save(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        itemHandler.load(tag);
        fluidHandler.load(tag);
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

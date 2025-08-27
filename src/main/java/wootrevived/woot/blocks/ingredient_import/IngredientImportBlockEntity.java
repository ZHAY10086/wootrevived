package wootrevived.woot.blocks.ingredient_import;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
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
    private final WootImportFluidHandler fluidHandler = new WootImportFluidHandler();

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

            IItemHandler neighborItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, direction.getOpposite());
            if(neighborItemHandler != null){
                for (int i = 0; i < neighborItemHandler.getSlots(); i++) {
                    ItemStack stack = neighborItemHandler.getStackInSlot(i);
                    ItemStack result = itemHandler.insertItem(i, stack, true);
                    if(result.getCount() < stack.getCount()){
                        neighborItemHandler.extractItem(i, stack.getCount() - result.getCount(), false);
                        itemHandler.insertItem(i, stack, false);
                    }
                }
            }

            IFluidHandler neighborFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos, direction.getOpposite());
            if(neighborFluidHandler != null){
                for (int i = 0; i < neighborFluidHandler.getTanks(); i++) {
                    FluidStack stack = neighborFluidHandler.getFluidInTank(i);
                    int filled = fluidHandler.fill(stack, IFluidHandler.FluidAction.SIMULATE);
                    if(filled > 0){
                        neighborFluidHandler.drain(new FluidStack(stack.getFluid(), filled), IFluidHandler.FluidAction.EXECUTE);
                        fluidHandler.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    public static IItemHandler getItemHandlerCapability(IngredientImportBlockEntity blockEntity, Direction side){
        return blockEntity.itemHandler;
    }

    public static IFluidHandler getFluidHandlerCapability(IngredientImportBlockEntity blockEntity, Direction side){
        return blockEntity.fluidHandler;
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

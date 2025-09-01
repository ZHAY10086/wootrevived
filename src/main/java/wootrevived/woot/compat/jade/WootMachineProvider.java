package wootrevived.woot.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import snownee.jade.addon.universal.FluidStorageProvider;
import snownee.jade.addon.universal.ItemCollector;
import snownee.jade.addon.universal.ItemIterator;
import snownee.jade.addon.universal.ItemStorageProvider;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.util.JadeForgeUtils;
import wootrevived.woot.Woot;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum WootMachineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag tag = blockAccessor.getServerData();
        if(tag.contains("Progress")){
            IElementHelper helper = IElementHelper.get();
            float progress = blockAccessor.getServerData().getInt("Progress") / 100F;

            BoxStyle.GradientBorder box = BoxStyle.getTransparent().clone();
            box.bgColor = 0x88000000;
            iTooltip.add(helper.progress(progress, null, helper.progressStyle(), box, false).size(new Vec2(10.0F, 4.0F)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Woot.location("machines");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if(blockAccessor.getBlockEntity() instanceof WootMachineBlockEntity machine) {
            putItemData(compoundTag, blockAccessor, machine);
            putFluidData(compoundTag, blockAccessor, machine);
            putProgressData(compoundTag, blockAccessor, machine);
        }
    }

    private void putItemData(CompoundTag compoundTag, BlockAccessor blockAccessor, WootMachineBlockEntity machine) {
        IItemHandler inventory = machine.getInventory();
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for(int i = 0 ; i < inventory.getSlots() ; i++)
            container.addItem(inventory.getStackInSlot(i));

        ItemCollector<?> items = new ItemCollector<>(new ItemIterator.ContainerItemIterator(0));
        List<ViewGroup<ItemStack>> viewsList = items.update(blockAccessor, blockAccessor.getLevel().getGameTime());

        if (ViewGroup.saveList(compoundTag, "JadeItemStorage", viewsList, (item) -> {
            CompoundTag itemTag = new CompoundTag();
            int count = item.getCount();
            if (count > 64)
                item.setCount(1);

            item.save(itemTag);
            if (count > 64) {
                itemTag.putInt("NewCount", count);
                item.setCount(count);
            }

            return itemTag;
        })) {
            compoundTag.putString("JadeItemStorageUid", ItemStorageProvider.INSTANCE.getUid().toString());
        }
    }

    private void putFluidData(CompoundTag compoundTag, BlockAccessor blockAccessor, WootMachineBlockEntity machine){
        CompoundTag inputFluid = null;
        int inputEmptyCapacity = 0;
        CompoundTag outputFluid = null;
        int outputEmptyCapacity = 0;

        if(machine.hasInputFluidCapability()){
            FluidTank tank = machine.getInputTank();
            int capacity = tank.getCapacity();
            if (capacity > 0) {
                FluidStack fluidStack = tank.getFluid();
                if (fluidStack.isEmpty()) {
                    inputEmptyCapacity = capacity;
                } else {
                    inputFluid = JadeForgeUtils.fromFluidStack(fluidStack, capacity);
                }
            }
        }

        if(machine.hasOutputFluidCapability()){
            FluidTank tank = machine.getOutputTank();
            int capacity = tank.getCapacity();
            if (capacity > 0) {
                FluidStack fluidStack = tank.getFluid();
                if (fluidStack.isEmpty()) {
                    outputEmptyCapacity = capacity;
                } else {
                    outputFluid = JadeForgeUtils.fromFluidStack(fluidStack, capacity);
                }
            }
        }

        if(inputFluid == null && inputEmptyCapacity > 0){
            inputFluid = JadeForgeUtils.fromFluidStack(FluidStack.EMPTY, inputEmptyCapacity);
        }

        if(outputFluid == null && outputEmptyCapacity > 0){
            outputFluid = JadeForgeUtils.fromFluidStack(FluidStack.EMPTY, outputEmptyCapacity);
        }

        List<CompoundTag> fluids = new ArrayList<>(2);

        if(inputFluid != null){
            fluids.add(inputFluid);
        }

        if(outputFluid != null){
            fluids.add(outputFluid);
        }

        if(ViewGroup.saveList(compoundTag, "JadeFluidStorage", fluids.isEmpty() ? null : List.of(new ViewGroup<>(fluids)), Function.identity())){
            compoundTag.putString("JadeFluidStorageUid", FluidStorageProvider.INSTANCE.getUid().toString());
        }
    }

    private void putProgressData(CompoundTag compoundTag, BlockAccessor blockAccessor, WootMachineBlockEntity machine){
        if(machine.isProcessActive()){
            compoundTag.putInt("Progress", machine.calculateProgress());
        }
    }
}

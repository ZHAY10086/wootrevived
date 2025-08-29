package wootrevived.woot.util.handlers;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.common.MachineSideProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WootItemHandlerWrapper implements IItemHandler {
    private final List<ItemWrapper> itemWrappers = new ArrayList<>();

    public WootItemHandlerWrapper addHandler(WootItemStackHandler item, Supplier<MachineSideProperty> property){
        itemWrappers.add(new ItemWrapper(item, property));
        return this;
    }

    @Override
    public int getSlots() {
        int size = 0;

        for(ItemWrapper itemWrapper : itemWrappers)
            size += itemWrapper.item.getSlots();

        return size;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        int size = 0;
        for(ItemWrapper itemWrapper : itemWrappers){
            WootItemStackHandler handler = itemWrapper.item;
            if(slot >= size + handler.getSlots()){
                size += handler.getSlots();
                continue;
            }

            return handler.getStackInSlot(slot - size);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        int size = 0;
        for(ItemWrapper itemWrapper : itemWrappers){
            WootItemStackHandler handler = itemWrapper.item;
            if(slot >= size + handler.getSlots()){
                size += handler.getSlots();
                continue;
            }

            MachineSideProperty property = itemWrapper.property().get();
            if(handler.isOutput || !handler.isItemValid(slot - size, stack) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PUSH)
                break;

            return handler.insertItem(slot - size, stack, simulate);
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        int size = 0;
        for(ItemWrapper itemWrapper : itemWrappers){
            WootItemStackHandler handler = itemWrapper.item;
            if(slot >= size + handler.getSlots()){
                size += handler.getSlots();
                continue;
            }

            MachineSideProperty property = itemWrapper.property().get();
            if(property == MachineSideProperty.DISABLED || property == MachineSideProperty.PULL)
                break;

            return handler.extractItem(slot - size, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        int size = 0;
        for(ItemWrapper itemWrapper : itemWrappers){
            WootItemStackHandler handler = itemWrapper.item;
            if(slot >= size + handler.getSlots()){
                size += handler.getSlots();
                continue;
            }

            return handler.getSlotLimit(slot - size);
        }
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int size = 0;
        for(ItemWrapper itemWrapper : itemWrappers){
            WootItemStackHandler handler = itemWrapper.item;
            if(slot >= size + handler.getSlots()){
                size += handler.getSlots();
                continue;
            }

            return handler.isItemValid(slot - size, stack);
        }
        return false;
    }

    private record ItemWrapper(
            WootItemStackHandler item,
            Supplier<MachineSideProperty> property
    ) {}
}

package wootrevived.woot.util.handlers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.EnergyStorage;
import wootrevived.woot.util.entity.WootTags;

public class WootEnergyStorage extends EnergyStorage implements INBTSerializable<Tag> {
    public WootEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    protected void onEnergyChanged() {}

    public void setEnergy(int energy) {
        this.energy = energy;
        onEnergyChanged();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int res = super.receiveEnergy(maxReceive, simulate);
        onEnergyChanged();
        return res;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int res = super.extractEnergy(maxExtract, simulate);
        onEnergyChanged();
        return res;
    }

    public int internalExtractEnergy(int maxExtract, boolean simulate)
    {
        if (maxExtract <= 0) {
            return 0;
        }

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(WootTags.ENERGY_TAG, getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if(nbt instanceof CompoundTag tag) {
            setEnergy(tag.getInt(WootTags.ENERGY_TAG));
        }
    }
}

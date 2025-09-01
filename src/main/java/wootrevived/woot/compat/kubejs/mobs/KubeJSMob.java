package wootrevived.woot.compat.kubejs.mobs;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.interfaces.WootDropsProperties;

import java.util.List;
import java.util.function.BiConsumer;

public class KubeJSMob<T extends Entity> extends WootFactoryMob<T> {
    private final WootFactoryMob<T> mob;
    private final List<ItemStack> importItems;
    private final List<FluidStack> importFluids;
    private final BiConsumer<String, WootDropsPropertiesJS> modifyDrops;

    public KubeJSMob(EntityType<T> entityType, Properties properties,
                     WootFactoryMob<T> mob,
                     List<ItemStack> importItems, List<FluidStack> importFluids,
                     @Nullable BiConsumer<String, WootDropsPropertiesJS> modifyDrops) {
        super(entityType, properties);
        this.mob = mob;
        this.importItems = importItems;
        this.importFluids = importFluids;
        this.modifyDrops = modifyDrops;
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        return mob.getDisplayName(mobTag, lookupProvider);
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, HolderLookup.Provider lookupProvider) {
        return mob.getTooltipKillName(mobTag, lookupProvider);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        return mob.saveTag(mobTag, lookupProvider);
    }

    @Override
    public boolean isSame(CompoundTag shardTag, CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        return mob.isSame(shardTag, mobTag, lookupProvider);
    }

    @Override
    public void modifyDrops(Phase phase, WootDropsProperties properties) {
        if(modifyDrops == null) {
            mob.modifyDrops(phase, properties);
            return;
        }

        modifyDrops.accept(phase.name(), new WootDropsPropertiesJS(properties));
    }

    @Override
    public List<ItemStack> getImportItems(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        if(importItems.isEmpty())
            return mob.getImportItems(mobTag, lookupProvider);

        return importItems;
    }

    @Override
    public List<FluidStack> getImportFluids(CompoundTag mobTag, HolderLookup.Provider lookupProvider){
        if(importFluids.isEmpty())
            return mob.getImportFluids(mobTag, lookupProvider);

        return importFluids;
    }
}

package wootrevived.woot.compat.kubejs.mobs;

import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.woot.registries.WootFactoryMobsRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WootFactoryMobEventJS extends StartupEventJS {
    private final WootFactoryMobRegistration registration;
    private final EntityType<?> entityType;

    public WootFactoryMobEventJS(WootFactoryMobRegistration registration, EntityType<?> entityType) {
        this.registration = registration;
        this.entityType = entityType;
    }

    public void registerDropsModifier(Consumer<WootDropsPropertiesJS> callback){
        registration.registerDropsModifier(entityType, properties -> {
            callback.accept(new WootDropsPropertiesJS(properties));
        });
    }

    public void blacklistMob(){
        registration.registerFactoryMob(new WootFactoryMob<>(entityType, new WootFactoryMob.Properties().blacklist(true)));
    }

    public FactoryMobBuilderJS<?> factoryMobPatcher(){
        return new FactoryMobBuilderJS<>(registration, entityType);
    }

    public static class FactoryMobBuilderJS<T extends Entity> {
        private final WootFactoryMobRegistration registration;
        private final EntityType<T> entityType;
        private final WootFactoryMob<T> mob;
        private final WootFactoryMob.Properties properties;
        private final List<ItemStack> importItems = new ArrayList<>();
        private final List<FluidStack> importFluids = new ArrayList<>();
        private BiConsumer<String, WootDropsPropertiesJS> modifyDrops = null;

        @SuppressWarnings("unchecked")
        public FactoryMobBuilderJS(WootFactoryMobRegistration registration, EntityType<T> entityType) {
            this.registration = registration;
            this.entityType = entityType;

            if(WootFactoryMobsRegistry.hasFactoryMob(entityType))
                mob = (WootFactoryMob<T>) WootFactoryMobsRegistry.getFactoryMob(entityType);
            else
                mob = new WootFactoryMob<>(entityType, new WootFactoryMob.Properties());

            properties = new WootFactoryMob.Properties()
                    .blacklist(mob.isBlacklisted())
                    .disabledSimulation(mob.isSimulationDisabled())
                    .rate(mob.getSpawnTickRate())
                    .vitalityCost(mob.getVitalityFuelCost())
                    .tier(mob.getTier());
        }

        public void patch(){
            registration.registerFactoryMob(new KubeJSMob<>(entityType, properties, mob, importItems, importFluids, modifyDrops));
        }

        public FactoryMobBuilderJS<T> modifyDrops(BiConsumer<String, WootDropsPropertiesJS> callback){
            modifyDrops = callback;
            return this;
        }

        public FactoryMobBuilderJS<T> setImportItems(OutputItem[] items){
            importItems.clear();
            importItems.addAll(Arrays.stream(items).map(o -> o.item).toList());
            return this;
        }

        public FactoryMobBuilderJS<T> setImportFluids(FluidStackJS[] items){
            importFluids.clear();
            importFluids.addAll(
                    Arrays.stream(items)
                            .map(FluidStackJS::getFluidStack)
                            .map(f -> new FluidStack(f.getFluid(), (int)f.getAmount(), f.getTag()))
                            .toList()
            );
            return this;
        }

        public FactoryMobBuilderJS<T> blacklist(boolean isBlacklisted){
            properties.blacklist(isBlacklisted);
            return this;
        }

        public FactoryMobBuilderJS<T> disabledSimulation(boolean isSimulationDisabled){
            properties.disabledSimulation(isSimulationDisabled);
            return this;
        }

        public FactoryMobBuilderJS<T> rate(int spawnTickRate){
            properties.rate(spawnTickRate);
            return this;
        }

        public FactoryMobBuilderJS<T> vitalityCost(int vitalityCost){
            properties.vitalityCost(vitalityCost);
            return this;
        }

        public FactoryMobBuilderJS<T> tier(String tier){
            properties.tier(StringRepresentable.fromEnum(Tier::values).byName(tier, Tier.INVALID));
            return this;
        }
    }
}

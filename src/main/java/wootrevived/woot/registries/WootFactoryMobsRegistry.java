package wootrevived.woot.registries;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.woot.init.WootPlugins;

import java.util.*;
import java.util.function.Consumer;

public class WootFactoryMobsRegistry extends WootFactoryMobRegistration {
    public static void register(){
        WootPlugins.registerFactoryMobs(new WootFactoryMobsRegistry());

        for(EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()){
            if(hasFactoryMob(entityType))
                continue;

            if(!entityType.canSerialize())
                continue;

            FACTORY_MOB_REGISTRY.put(entityType, new WootFactoryMob<>(entityType, new WootFactoryMob.Properties()));
        }
    }

    private static final Map<EntityType<?>, WootFactoryMob<?>> FACTORY_MOB_REGISTRY = new HashMap<>();

    @Override
    public void registerFactoryMob(WootFactoryMob<?> mob){
        FACTORY_MOB_REGISTRY.put(mob.getEntityType(), mob);
    }

    public static WootFactoryMob<?> getFactoryMob(CompoundTag mobTag){
        return getFactoryMob(mobTag.getString("id"));
    }

    public static WootFactoryMob<?> getFactoryMob(String mobId){
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(mobId));
        return getFactoryMob(entityType);
    }

    public static WootFactoryMob<?> getFactoryMob(EntityType<?> entityType){
        return FACTORY_MOB_REGISTRY.get(entityType);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasFactoryMob(CompoundTag mobTag){
        return hasFactoryMob(mobTag.getString("id"));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasFactoryMob(String mobId){
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(mobId));
        return hasFactoryMob(entityType);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasFactoryMob(EntityType<?> entityType){
        return FACTORY_MOB_REGISTRY.containsKey(entityType);
    }

    public static void removeFactoryMob(EntityType<?> entityType){
        FACTORY_MOB_REGISTRY.remove(entityType);
    }

    public static Collection<WootFactoryMob<?>> getFactoryMobValues(){
        return FACTORY_MOB_REGISTRY.values();
    }

    private static final Map<EntityType<?>, List<Consumer<WootDropsProperties>>> ITEM_DROPS_REGISTRY = new HashMap<>();

    @Override
    public void registerDropsModifier(EntityType<?> entityType, Consumer<WootDropsProperties> callback) {
        if(!ITEM_DROPS_REGISTRY.containsKey(entityType)) ITEM_DROPS_REGISTRY.put(entityType, new ArrayList<>());
        ITEM_DROPS_REGISTRY.get(entityType).add(callback);
    }

    public static List<Consumer<WootDropsProperties>> getDropsModifier(CompoundTag mobTag){
        return getDropsModifier(mobTag.getString("id"));
    }

    public static List<Consumer<WootDropsProperties>> getDropsModifier(String mobId){
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(mobId));
        return getDropsModifier(entityType);
    }

    public static List<Consumer<WootDropsProperties>> getDropsModifier(EntityType<?> entityType){
        if(!hasDropsModifier(entityType)) return List.of();
        return ITEM_DROPS_REGISTRY.get(entityType);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasDropsModifier(CompoundTag mobTag){
        return hasDropsModifier(mobTag.getString("id"));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasDropsModifier(String mobId){
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(mobId));
        return hasDropsModifier(entityType);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasDropsModifier(EntityType<?> entityType){
        return ITEM_DROPS_REGISTRY.containsKey(entityType);
    }
}

package wootrevived.woot.registries;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;
import wootrevived.woot.items.basic.BasicItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UpgradeItemsRegistry extends WootUpgradeItemRegistration {
    /* Woot Items */

    private final IEventBus wootBus;

    private UpgradeItemsRegistry(IEventBus wootBus){
        this.wootBus = wootBus;
    }

    private static final Map<String, RegistryObject<? extends WootUpgradeItem>> REGISTRY = new HashMap<>();

    @Override
    public void register(RegistryObject<? extends WootUpgradeItem> item) {
        if(item.getId() == null)
            return;
        REGISTRY.put(getNameFromItem(item), item);
        Registry.addToCreativeTab(item);
    }

    @Override
    public IEventBus getWootEventBus() {
        return wootBus;
    }

    public static RegistryObject<? extends WootUpgradeItem> get(String name){
        return REGISTRY.get(name);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean has(String name){
        return REGISTRY.containsKey(name);
    }

    public static Collection<RegistryObject<? extends WootUpgradeItem>> getValues(){
        return REGISTRY.values();
    }

    public static String getNameFromItem(RegistryObject<? extends WootUpgradeItem> item){
        return item.getId().toString().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    public static String getNameFromItem(WootUpgradeItem item){
        return ForgeRegistries.ITEMS.getKey(item).toString().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    /* Forge Items */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        Registry.addToCreativeTab(UPGRADE_BASE_ITEM);

        WootPlugins.registerUpgradeItems(new UpgradeItemsRegistry(bus));
    }

    /* Upgrade Base */

    public static final String UPGRADE_BASE_TAG = "upgrade_base";
    public static final RegistryObject<BasicItem> UPGRADE_BASE_ITEM = ITEMS.register(UPGRADE_BASE_TAG, () -> new BasicItem(BasicItem.Type.UPGRADE_BASE));
}

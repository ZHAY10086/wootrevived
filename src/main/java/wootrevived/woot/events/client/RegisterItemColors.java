package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.items.dye_casing.DyeCasingItem;
import wootrevived.woot.items.dye_plate.DyePlateItem;
import wootrevived.woot.registries.ItemsRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Woot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class RegisterItemColors {
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        event.register(
                (s, t) -> ((DyePlateItem)s.getItem()).getColor(),
                ItemsRegistry.WHITE_DYE_PLATE_ITEM.get(),
                ItemsRegistry.ORANGE_DYE_PLATE_ITEM.get(),
                ItemsRegistry.MAGENTA_DYE_PLATE_ITEM.get(),
                ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM.get(),
                ItemsRegistry.YELLOW_DYE_PLATE_ITEM.get(),
                ItemsRegistry.LIME_DYE_PLATE_ITEM.get(),
                ItemsRegistry.PINK_DYE_PLATE_ITEM.get(),
                ItemsRegistry.GRAY_DYE_PLATE_ITEM.get(),
                ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM.get(),
                ItemsRegistry.CYAN_DYE_PLATE_ITEM.get(),
                ItemsRegistry.PURPLE_DYE_PLATE_ITEM.get(),
                ItemsRegistry.BLUE_DYE_PLATE_ITEM.get(),
                ItemsRegistry.BROWN_DYE_PLATE_ITEM.get(),
                ItemsRegistry.GREEN_DYE_PLATE_ITEM.get(),
                ItemsRegistry.RED_DYE_PLATE_ITEM.get(),
                ItemsRegistry.BLACK_DYE_PLATE_ITEM.get()
        );

        event.register(
                (s, t) -> ((DyeCasingItem)s.getItem()).getColor(),
                ItemsRegistry.WHITE_DYE_CASING_ITEM.get(),
                ItemsRegistry.ORANGE_DYE_CASING_ITEM.get(),
                ItemsRegistry.MAGENTA_DYE_CASING_ITEM.get(),
                ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM.get(),
                ItemsRegistry.YELLOW_DYE_CASING_ITEM.get(),
                ItemsRegistry.LIME_DYE_CASING_ITEM.get(),
                ItemsRegistry.PINK_DYE_CASING_ITEM.get(),
                ItemsRegistry.GRAY_DYE_CASING_ITEM.get(),
                ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM.get(),
                ItemsRegistry.CYAN_DYE_CASING_ITEM.get(),
                ItemsRegistry.PURPLE_DYE_CASING_ITEM.get(),
                ItemsRegistry.BLUE_DYE_CASING_ITEM.get(),
                ItemsRegistry.BROWN_DYE_CASING_ITEM.get(),
                ItemsRegistry.GREEN_DYE_CASING_ITEM.get(),
                ItemsRegistry.RED_DYE_CASING_ITEM.get(),
                ItemsRegistry.BLACK_DYE_CASING_ITEM.get()
        );
    }
}

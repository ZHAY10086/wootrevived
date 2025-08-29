package wootrevived.woot.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import wootrevived.woot.Woot;
import wootrevived.woot.network.WootFakeSpawnerUpdate;
import wootrevived.woot.network.WootMachineUpdate;

@Mod.EventBusSubscriber(modid = Woot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterPayloadHandlers {
    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(Woot.MOD_ID).versioned("1").optional();

        registrar.play(WootMachineUpdate.ID, WootMachineUpdate::read, handler -> handler.server(WootMachineUpdate::handle));
        registrar.play(WootFakeSpawnerUpdate.ID, WootFakeSpawnerUpdate::read, handler -> handler.server(WootFakeSpawnerUpdate::handle));
    }
}

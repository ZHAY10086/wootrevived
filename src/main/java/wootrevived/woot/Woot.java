package wootrevived.woot;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import wootrevived.woot.guide.WootGuide;
import wootrevived.woot.init.CommonConfig;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;

@Mod(Woot.MOD_ID)
public class Woot
{
    public static final String MOD_ID = "woot_revived";

    public Woot()
    {
        CommonConfig.init();

        WootPlugins.registerPlugins();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(bus);

        WootGuide.init();
    }
}

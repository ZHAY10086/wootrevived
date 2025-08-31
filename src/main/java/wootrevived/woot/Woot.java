package wootrevived.woot;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import wootrevived.woot.guide.WootGuide;
import wootrevived.woot.init.CommonConfig;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;

@Mod(Woot.MOD_ID)
public class Woot
{
    public static final String MOD_ID = "woot_revived";

    public Woot(IEventBus bus)
    {
        CommonConfig.init();

        WootPlugins.registerPlugins();

        Registry.register(bus);

        WootGuide.init();
    }
}

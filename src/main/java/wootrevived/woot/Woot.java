package wootrevived.woot;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.guide.WootGuide;
import wootrevived.woot.init.CommonConfig;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;

import java.util.Objects;

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

    public static @NotNull ResourceLocation location(String path) {
        return Objects.requireNonNull(ResourceLocation.tryBuild(Woot.MOD_ID, path));
    }
}

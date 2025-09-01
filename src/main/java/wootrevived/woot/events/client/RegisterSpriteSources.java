package wootrevived.woot.events.client;

import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.client.sprite.factory_upgrade.FactoryUpgradeDynamicSpriteSource;
import wootrevived.woot.registries.BlocksRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Woot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class RegisterSpriteSources {
    private static SpriteSourceType upgradeLoader;

    public static SpriteSourceType getUpgradeLoader() {
        return upgradeLoader;
    }

    @SubscribeEvent
    public static void registerSpriteSourceTypes(RegisterSpriteSourceTypesEvent event){
        upgradeLoader = event.register(Woot.location(BlocksRegistry.FACTORY_UPGRADE_TAG), FactoryUpgradeDynamicSpriteSource.CODEC);
    }
}

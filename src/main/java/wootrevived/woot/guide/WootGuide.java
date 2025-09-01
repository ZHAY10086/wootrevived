package wootrevived.woot.guide;

import guideme.Guide;
import guideme.compiler.tags.BlockTagCompiler;
import guideme.compiler.tags.FlowTagCompiler;
import net.minecraft.resources.ResourceLocation;
import wootrevived.woot.Woot;
import wootrevived.woot.guide.recipes.EnchantedRecipeCompiler;

public class WootGuide {
    public static ResourceLocation ID = Woot.location("guide");

    public static void init() {
        Guide.builder(ID)
                .folder("guidebook")
                .extension(BlockTagCompiler.EXTENSION_POINT, new EnchantedRecipeCompiler())
                .extension(BlockTagCompiler.EXTENSION_POINT, new TierMobsCompiler())
                .extension(FlowTagCompiler.EXTENSION_POINT, new ConfigPropertyCompiler())
                .build();
    }
}

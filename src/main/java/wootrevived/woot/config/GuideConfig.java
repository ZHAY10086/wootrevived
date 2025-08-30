package wootrevived.woot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GuideConfig {
    public static ForgeConfigSpec.BooleanValue GIVE_ON_SPAWN;

    public static void build(ForgeConfigSpec.Builder builder){
        builder.comment("Guide").push("guide");
        {
            GIVE_ON_SPAWN = builder.comment(String.format("Should give the woot guide book on player first connection [Default: %b]", DefaultsConfig.Guide.GIVE_ON_SPAWN))
                    .define("shouldGiveOnSpawn", DefaultsConfig.Guide.GIVE_ON_SPAWN);
        }
        builder.pop();
    }
}

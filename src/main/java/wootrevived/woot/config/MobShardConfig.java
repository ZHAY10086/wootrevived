package wootrevived.woot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MobShardConfig {
    public static ForgeConfigSpec.ConfigValue<Integer> NUM_OF_KILLS;

    public static void build(ForgeConfigSpec.Builder builder){
        builder.comment("Mob Shard").push("mob_shard");
        {
            NUM_OF_KILLS = builder.comment(String.format("Number of kills to program the shard [Default: %d]", DefaultsConfig.MobShard.NUM_OF_KILLS))
                    .define("numOfKills", DefaultsConfig.MobShard.NUM_OF_KILLS);
        }
        builder.pop();
    }
}

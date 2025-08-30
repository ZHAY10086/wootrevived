package wootrevived.woot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CellConfig {
    public static ForgeConfigSpec.ConfigValue<Integer> COPPER_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> IRON_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> GOLD_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> DIAMOND_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> NETHERITE_CAPACITY;

    public static void build(ForgeConfigSpec.Builder builder){
        builder.comment("Cell").push("cell");
        {
            COPPER_CAPACITY = builder.comment(String.format("The tank capacity of the Copper Vitality Cell [Default: %d]", DefaultsConfig.Cell.COPPER_CAPACITY))
                    .define("copperCapacity", DefaultsConfig.Cell.COPPER_CAPACITY);

            IRON_CAPACITY = builder.comment(String.format("The tank capacity of the Iron Vitality Cell [Default: %d]", DefaultsConfig.Cell.IRON_CAPACITY))
                    .define("ironCapacity", DefaultsConfig.Cell.IRON_CAPACITY);

            GOLD_CAPACITY = builder.comment(String.format("The tank capacity of the Gold Vitality Cell [Default: %d]", DefaultsConfig.Cell.GOLD_CAPACITY))
                    .define("goldCapacity", DefaultsConfig.Cell.GOLD_CAPACITY);

            DIAMOND_CAPACITY = builder.comment(String.format("The tank capacity of the Diamond Vitality Cell [Default: %d]", DefaultsConfig.Cell.DIAMOND_CAPACITY))
                    .define("diamondCapacity", DefaultsConfig.Cell.DIAMOND_CAPACITY);

            NETHERITE_CAPACITY = builder.comment(String.format("The tank capacity of the Netherite Vitality Cell [Default: %d]", DefaultsConfig.Cell.NETHERITE_CAPACITY))
                    .define("netheriteCapacity", DefaultsConfig.Cell.NETHERITE_CAPACITY);
        }
        builder.pop();
    }
}

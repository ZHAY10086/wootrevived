package wootrevived.woot.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DyeLiquifierConfig {
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_MAX_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_PROCESS_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> OUTPUT_TANK_CAPACITY;

    public static ModConfigSpec.ConfigValue<Integer> RED_TANK_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> YELLOW_TANK_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> BLUE_TANK_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> WHITE_TANK_CAPACITY;

    public static void build(ModConfigSpec.Builder builder){
        builder.comment("Dye Liquifier").push("dye_liquifier");
        {
            ENERGY_CAPACITY = builder.comment(String.format("The energy capacity of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.ENERGY_CAPACITY))
                    .define("energyCapacity", DefaultsConfig.DyeLiquifier.ENERGY_CAPACITY);

            ENERGY_MAX_TRANSFER = builder.comment(String.format("The max energy in/out transfer rate of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.ENERGY_MAX_TRANSFER))
                    .define("energyMaxTransfer", DefaultsConfig.DyeLiquifier.ENERGY_MAX_TRANSFER);

            ENERGY_PROCESS_TRANSFER = builder.comment(String.format("The energy transfer rate of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.ENERGY_PROCESS_TRANSFER))
                    .define("energyProcessTransfer", DefaultsConfig.DyeLiquifier.ENERGY_PROCESS_TRANSFER);

            OUTPUT_TANK_CAPACITY = builder.comment(String.format("The output tank capacity of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.OUTPUT_TANK_CAPACITY))
                    .define("outputTankCapacity", DefaultsConfig.DyeLiquifier.OUTPUT_TANK_CAPACITY);

            RED_TANK_CAPACITY = builder.comment(String.format("The internal red tank capacity of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.RED_TANK_CAPACITY))
                    .define("redTankCapacity", DefaultsConfig.DyeLiquifier.RED_TANK_CAPACITY);

            YELLOW_TANK_CAPACITY = builder.comment(String.format("The internal yellow tank capacity of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.YELLOW_TANK_CAPACITY))
                    .define("yellowTankCapacity", DefaultsConfig.DyeLiquifier.YELLOW_TANK_CAPACITY);

            BLUE_TANK_CAPACITY = builder.comment(String.format("The internal blue tank capacity of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.BLUE_TANK_CAPACITY))
                    .define("blueTankCapacity", DefaultsConfig.DyeLiquifier.BLUE_TANK_CAPACITY);

            WHITE_TANK_CAPACITY = builder.comment(String.format("The internal white tank capacity of the Dye Liquifier [Default: %d]", DefaultsConfig.DyeLiquifier.WHITE_TANK_CAPACITY))
                    .define("whiteTankCapacity", DefaultsConfig.DyeLiquifier.WHITE_TANK_CAPACITY);
        }
        builder.pop();
    }
}
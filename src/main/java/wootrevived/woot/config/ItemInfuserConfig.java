package wootrevived.woot.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ItemInfuserConfig {
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_MAX_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_PROCESS_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> FLUID_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> INPUT_TANK_CAPACITY;

    public static void build(ModConfigSpec.Builder builder){
        builder.comment("Item Infuser").push("item_infuser");
        {
            ENERGY_CAPACITY = builder.comment(String.format("The energy capacity of the Item Infuser [Default: %d]", DefaultsConfig.ItemInfuser.ENERGY_CAPACITY))
                    .define("energyCapacity", DefaultsConfig.ItemInfuser.ENERGY_CAPACITY);

            ENERGY_MAX_TRANSFER = builder.comment(String.format("The max energy in/out transfer rate of the Item Infuser [Default: %d]", DefaultsConfig.ItemInfuser.ENERGY_MAX_TRANSFER))
                    .define("energyMaxTransfer", DefaultsConfig.ItemInfuser.ENERGY_MAX_TRANSFER);

            ENERGY_PROCESS_TRANSFER = builder.comment(String.format("The energy transfer rate of the Item Infuser [Default: %d]", DefaultsConfig.ItemInfuser.ENERGY_PROCESS_TRANSFER))
                    .define("energyProcessTransfer", DefaultsConfig.ItemInfuser.ENERGY_PROCESS_TRANSFER);

            FLUID_TRANSFER = builder.comment(String.format("The fluid transfer rate of the Item Infuser [Default: %d]", DefaultsConfig.ItemInfuser.FLUID_TRANSFER))
                    .define("fluidTransfer", DefaultsConfig.ItemInfuser.FLUID_TRANSFER);

            INPUT_TANK_CAPACITY = builder.comment(String.format("The input tank capacity of the Item Infuser [Default: %d]", DefaultsConfig.ItemInfuser.INPUT_TANK_CAPACITY))
                    .define("inputTankCapacity", DefaultsConfig.ItemInfuser.INPUT_TANK_CAPACITY);
        }
        builder.pop();
    }
}
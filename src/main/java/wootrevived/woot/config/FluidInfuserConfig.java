package wootrevived.woot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FluidInfuserConfig {
    public static ForgeConfigSpec.ConfigValue<Integer> ENERGY_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> ENERGY_MAX_TRANSFER;
    public static ForgeConfigSpec.ConfigValue<Integer> ENERGY_PROCESS_TRANSFER;
    public static ForgeConfigSpec.ConfigValue<Integer> FLUID_TRANSFER;
    public static ForgeConfigSpec.ConfigValue<Integer> INPUT_TANK_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> OUTPUT_TANK_CAPACITY;

    public static void build(ForgeConfigSpec.Builder builder){
        builder.comment("Fluid Infuser").push("fluid_infuser");
        {
            ENERGY_CAPACITY = builder.comment(String.format("The energy capacity of the Fluid Infuser [Default: %d]", DefaultsConfig.FluidInfuser.ENERGY_CAPACITY))
                    .define("energyCapacity", DefaultsConfig.FluidInfuser.ENERGY_CAPACITY);

            ENERGY_MAX_TRANSFER = builder.comment(String.format("The max energy in/out transfer rate of the Fluid Infuser [Default: %d]", DefaultsConfig.FluidInfuser.ENERGY_MAX_TRANSFER))
                    .define("energyMaxTransfer", DefaultsConfig.FluidInfuser.ENERGY_MAX_TRANSFER);

            ENERGY_PROCESS_TRANSFER = builder.comment(String.format("The energy transfer rate of the Fluid Infuser [Default: %d]", DefaultsConfig.FluidInfuser.ENERGY_PROCESS_TRANSFER))
                    .define("energyProcessTransfer", DefaultsConfig.FluidInfuser.ENERGY_PROCESS_TRANSFER);

            FLUID_TRANSFER = builder.comment(String.format("The fluid transfer rate of the Fluid Infuser [Default: %d]", DefaultsConfig.FluidInfuser.FLUID_TRANSFER))
                    .define("fluidTransfer", DefaultsConfig.FluidInfuser.FLUID_TRANSFER);

            INPUT_TANK_CAPACITY = builder.comment(String.format("The input tank capacity of the Fluid Infuser [Default: %d]", DefaultsConfig.FluidInfuser.INPUT_TANK_CAPACITY))
                    .define("inputTankCapacity", DefaultsConfig.FluidInfuser.INPUT_TANK_CAPACITY);

            OUTPUT_TANK_CAPACITY = builder.comment(String.format("The output tank capacity of the Fluid Infuser [Default: %d]", DefaultsConfig.FluidInfuser.OUTPUT_TANK_CAPACITY))
                    .define("outputTankCapacity", DefaultsConfig.FluidInfuser.OUTPUT_TANK_CAPACITY);
        }
        builder.pop();
    }
}

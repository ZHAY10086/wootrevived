package wootrevived.woot.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EnchantedLiquifierConfig {
    public static ModConfigSpec.ConfigValue<Integer> PER_ENCHANT_FLUID;
    public static ModConfigSpec.ConfigValue<Integer> PER_ENCHANT_ENERGY;
    public static ModConfigSpec.ConfigValue<Integer> MAX_ENCHANT_LVL;

    public static ModConfigSpec.ConfigValue<Integer> ENERGY_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_MAX_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> ENERGY_PROCESS_TRANSFER;
    public static ModConfigSpec.ConfigValue<Integer> OUTPUT_TANK_CAPACITY;

    public static void build(ModConfigSpec.Builder builder){
        builder.comment("Enchanted Liquifier").push("enchanted_liquifier");
        {
            PER_ENCHANT_FLUID = builder.comment(String.format("The enchanted fluid produced per enchant level [Default: %d]", DefaultsConfig.EnchantedLiquifier.PER_ENCHANT_FLUID))
                    .define("perEnchantFluid", DefaultsConfig.EnchantedLiquifier.PER_ENCHANT_FLUID);

            PER_ENCHANT_ENERGY = builder.comment(String.format("The energy used per enchant level [Default: %d]", DefaultsConfig.EnchantedLiquifier.PER_ENCHANT_ENERGY))
                    .define("perEnchantEnergy", DefaultsConfig.EnchantedLiquifier.PER_ENCHANT_ENERGY);

            MAX_ENCHANT_LVL = builder.comment(String.format("The max level of enchantment that can be converted [Default: %d]", DefaultsConfig.EnchantedLiquifier.MAX_ENCHANT_LVL))
                    .define("maxEnchantLevel", DefaultsConfig.EnchantedLiquifier.MAX_ENCHANT_LVL);

            ENERGY_CAPACITY = builder.comment(String.format("The energy capacity of the Enchanted Liquifier [Default: %d]", DefaultsConfig.EnchantedLiquifier.ENERGY_CAPACITY))
                    .define("energyCapacity", DefaultsConfig.EnchantedLiquifier.ENERGY_CAPACITY);

            ENERGY_MAX_TRANSFER = builder.comment(String.format("The max energy in/out transfer rate of the Enchanted Liquifier [Default: %d]", DefaultsConfig.EnchantedLiquifier.ENERGY_MAX_TRANSFER))
                    .define("energyMaxTransfer", DefaultsConfig.EnchantedLiquifier.ENERGY_MAX_TRANSFER);

            ENERGY_PROCESS_TRANSFER = builder.comment(String.format("The energy transfer rate of the Enchanted Liquifier [Default: %d]", DefaultsConfig.EnchantedLiquifier.ENERGY_PROCESS_TRANSFER))
                    .define("energyProcessTransfer", DefaultsConfig.EnchantedLiquifier.ENERGY_PROCESS_TRANSFER);

            OUTPUT_TANK_CAPACITY = builder.comment(String.format("The output tank capacity of the Enchanted Liquifier [Default: %d]", DefaultsConfig.EnchantedLiquifier.OUTPUT_TANK_CAPACITY))
                    .define("outputTankCapacity", DefaultsConfig.EnchantedLiquifier.OUTPUT_TANK_CAPACITY);
        }
        builder.pop();
    }
}
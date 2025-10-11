package wootrevived.woot.config;

public class DefaultsConfig {
    public static final int BUCKET_CAPACITY = 1000;

    public static class Cell {
        public static final int COPPER_CAPACITY = BUCKET_CAPACITY * 10;
        public static final int IRON_CAPACITY = BUCKET_CAPACITY * 50;
        public static final int GOLD_CAPACITY = BUCKET_CAPACITY * 100;
        public static final int DIAMOND_CAPACITY = BUCKET_CAPACITY * 500;
        public static final int NETHERITE_CAPACITY = BUCKET_CAPACITY * 1000;
    }

    public static class DyeLiquifier {
        public static final int ENERGY_CAPACITY = 10000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 25;
        public static final int OUTPUT_TANK_CAPACITY = BUCKET_CAPACITY * 10;

        public static final int COLOR_PRODUCE_AMOUNT = 72;
        public static final int PURE_DYE_PRODUCE_AMOUNT = COLOR_PRODUCE_AMOUNT * 4;

        public static final int RED_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
        public static final int YELLOW_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
        public static final int BLUE_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
        public static final int WHITE_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
    }

    public static class EnchantedLiquifier {
        public static final int ENERGY_CAPACITY = 50000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 100;
        public static final int OUTPUT_TANK_CAPACITY = BUCKET_CAPACITY * 25;

        public static final int PER_ENCHANT_FLUID = BUCKET_CAPACITY;
        public static final int PER_ENCHANT_ENERGY = 4000;
        public static final int MAX_ENCHANT_LVL = 10;
    }

    public static class FluidInfuser {
        public static final int ENERGY_CAPACITY = 10000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 50;
        public static final int INPUT_TANK_CAPACITY = BUCKET_CAPACITY * 10;
        public static final int OUTPUT_TANK_CAPACITY = BUCKET_CAPACITY * 10;
    }

    public static class ItemInfuser {
        public static final int ENERGY_CAPACITY = 10000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 50;
        public static final int INPUT_TANK_CAPACITY = BUCKET_CAPACITY * 10;
    }

    public static class Guide {
        public static final boolean GIVE_ON_SPAWN = true;
    }

    public static class MobShard {
        public static final int NUM_OF_KILLS = 5;
    }
}

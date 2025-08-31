package wootrevived.woot.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wootrevived.woot.Woot;
import wootrevived.woot.init.Registry;
import wootrevived.woot.util.fluid.WootBucketItem;
import wootrevived.woot.util.fluid.WootFluidType;

public class FluidsRegistry {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Woot.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, Woot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.Keys.BLOCKS, Woot.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);

        for(RegistryObject<Item> item : ITEMS.getEntries())
            Registry.addToCreativeTab(item);
    }

    /* Default Properties */

    public static final ResourceLocation UNDERWATER_OVERLAY_RL = ResourceLocation.tryParse("misc/underwater");

    public static final FluidType.Properties defaultProperties = FluidType.Properties.create()
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .canSwim(true)
            .canDrown(true)
            .canPushEntity(true)
            .supportsBoating(true);

    /* Vitality Fuel */

    public static String VITALITY_FUEL_FLUID_TAG = "vitality_fuel_fluid";
    public static final ResourceLocation VITALITY_FUEL_STILL_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/vitality_fuel_still");
    public static final ResourceLocation VITALITY_FUEL_FLOW_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/vitality_fuel_flow");
    public static RegistryObject<FluidType> VITALITY_FUEL_FLUID_TYPE = FLUID_TYPES.register(VITALITY_FUEL_FLUID_TAG,
            () -> new WootFluidType(
                    VITALITY_FUEL_STILL_TEX, VITALITY_FUEL_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0x2E, 0xB8, 0xB8,
                    defaultProperties
            )
    );
    public static final RegistryObject<FlowingFluid> SOURCE_VITALITY_FUEL_FLUID = FLUIDS.register(VITALITY_FUEL_FLUID_TAG, () -> new ForgeFlowingFluid.Source(FluidsRegistry.VITALITY_FUEL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_VITALITY_FUEL_FLUID = FLUIDS.register(VITALITY_FUEL_FLUID_TAG + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidsRegistry.VITALITY_FUEL_FLUID_PROPERTIES));
    public static final RegistryObject<LiquidBlock> VITALITY_FUEL_FLUID_BLOCK = BLOCKS.register(VITALITY_FUEL_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_VITALITY_FUEL_FLUID, BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WATER).noLootTable()));
    public static final RegistryObject<BucketItem> VITALITY_FUEL_FLUID_BUCKET = ITEMS.register(VITALITY_FUEL_FLUID_TAG + "_bucket", () -> new WootBucketItem(SOURCE_VITALITY_FUEL_FLUID));
    public static final ForgeFlowingFluid.Properties VITALITY_FUEL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(VITALITY_FUEL_FLUID_TYPE, SOURCE_VITALITY_FUEL_FLUID, FLOWING_VITALITY_FUEL_FLUID)
            .block(VITALITY_FUEL_FLUID_BLOCK)
            .bucket(VITALITY_FUEL_FLUID_BUCKET);

    /* Pure Dye */

    public static String PURE_DYE_FLUID_TAG = "pure_dye_fluid";
    public static final ResourceLocation PURE_DYE_STILL_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/pure_dye_still");
    public static final ResourceLocation PURE_DYE_FLOW_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/pure_dye_flow");
    public static RegistryObject<FluidType> PURE_DYE_FLUID_TYPE = FLUID_TYPES.register(PURE_DYE_FLUID_TAG,
            () -> new WootFluidType(
                    PURE_DYE_STILL_TEX, PURE_DYE_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0xE9, 0xE9, 0xE9,
                    defaultProperties
            )
    );
    public static final RegistryObject<FlowingFluid> SOURCE_PURE_DYE_FLUID = FLUIDS.register(PURE_DYE_FLUID_TAG, () -> new ForgeFlowingFluid.Source(FluidsRegistry.PURE_DYE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PURE_DYE_FLUID = FLUIDS.register(PURE_DYE_FLUID_TAG + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidsRegistry.PURE_DYE_FLUID_PROPERTIES));
    public static final RegistryObject<LiquidBlock> PURE_DYE_FLUID_BLOCK = BLOCKS.register(PURE_DYE_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_PURE_DYE_FLUID, BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WATER).noLootTable()));
    public static final RegistryObject<BucketItem> PURE_DYE_FLUID_BUCKET = ITEMS.register(PURE_DYE_FLUID_TAG + "_bucket", () -> new WootBucketItem(SOURCE_PURE_DYE_FLUID));
    public static final ForgeFlowingFluid.Properties PURE_DYE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(PURE_DYE_FLUID_TYPE, SOURCE_PURE_DYE_FLUID, FLOWING_PURE_DYE_FLUID)
            .block(PURE_DYE_FLUID_BLOCK)
            .bucket(PURE_DYE_FLUID_BUCKET);

    /* Enchanted */

    public static String ENCHANTED_FLUID_TAG = "enchanted_fluid";
    public static final ResourceLocation ENCHANTED_STILL_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/enchanted_still");
    public static final ResourceLocation ENCHANTED_FLOW_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/enchanted_flow");
    public static RegistryObject<FluidType> ENCHANTED_FLUID_TYPE = FLUID_TYPES.register(ENCHANTED_FLUID_TAG,
            () -> new WootFluidType(
                    ENCHANTED_STILL_TEX, ENCHANTED_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0x35, 0x4C, 0xD5,
                    defaultProperties
            )
    );
    public static final RegistryObject<FlowingFluid> SOURCE_ENCHANTED_FLUID = FLUIDS.register(ENCHANTED_FLUID_TAG, () -> new ForgeFlowingFluid.Source(FluidsRegistry.ENCHANTED_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ENCHANTED_FLUID = FLUIDS.register(ENCHANTED_FLUID_TAG + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidsRegistry.ENCHANTED_FLUID_PROPERTIES));
    public static final RegistryObject<LiquidBlock> ENCHANTED_FLUID_BLOCK = BLOCKS.register(ENCHANTED_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_ENCHANTED_FLUID, BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WATER).noLootTable()));
    public static final RegistryObject<WootBucketItem> ENCHANTED_FLUID_BUCKET = ITEMS.register(ENCHANTED_FLUID_TAG + "_bucket", () -> new WootBucketItem(SOURCE_ENCHANTED_FLUID));
    public static final ForgeFlowingFluid.Properties ENCHANTED_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(ENCHANTED_FLUID_TYPE, SOURCE_ENCHANTED_FLUID, FLOWING_ENCHANTED_FLUID)
            .block(ENCHANTED_FLUID_BLOCK)
            .bucket(ENCHANTED_FLUID_BUCKET);

    /* Mob Tears */

    public static String MOB_TEARS_FLUID_TAG = "mob_tears_fluid";
    public static final ResourceLocation MOB_TEARS_STILL_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/mob_tears_still");
    public static final ResourceLocation MOB_TEARS_FLOW_TEX = ResourceLocation.tryBuild(Woot.MOD_ID, "block/mob_tears_flow");
    public static RegistryObject<FluidType> MOB_TEARS_FLUID_TYPE = FLUID_TYPES.register(MOB_TEARS_FLUID_TAG,
            () -> new WootFluidType(
                    MOB_TEARS_STILL_TEX, MOB_TEARS_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0x5E, 0xB8, 0x21,
                    defaultProperties
            )
    );
    public static final RegistryObject<FlowingFluid> SOURCE_MOB_TEARS_FLUID = FLUIDS.register(MOB_TEARS_FLUID_TAG, () -> new ForgeFlowingFluid.Source(FluidsRegistry.MOB_TEARS_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_MOB_TEARS_FLUID = FLUIDS.register(MOB_TEARS_FLUID_TAG + "_flowing", () -> new ForgeFlowingFluid.Flowing(FluidsRegistry.MOB_TEARS_FLUID_PROPERTIES));
    public static final RegistryObject<LiquidBlock> MOB_TEARS_FLUID_BLOCK = BLOCKS.register(MOB_TEARS_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_MOB_TEARS_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
    public static final RegistryObject<BucketItem> MOB_TEARS_FLUID_BUCKET = ITEMS.register(MOB_TEARS_FLUID_TAG + "_bucket", () -> new WootBucketItem(SOURCE_MOB_TEARS_FLUID));
    public static final ForgeFlowingFluid.Properties MOB_TEARS_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(MOB_TEARS_FLUID_TYPE, SOURCE_MOB_TEARS_FLUID, FLOWING_MOB_TEARS_FLUID)
            .block(MOB_TEARS_FLUID_BLOCK)
            .bucket(MOB_TEARS_FLUID_BUCKET);
}

package wootrevived.woot.blocks.fake_spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.woot.network.WootFakeSpawnerUpdate;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidTankHandler;

public class FakeSpawnerBlockEntity extends FactoryBlockBaseEntity {
    public FakeSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FAKE_SPAWNER_BLOCK_ENTITY.get(), pos, state);
    }

    private CompoundTag mobTag = null;

    public @Nullable CompoundTag getMobTag() {
        return mobTag;
    }

    public @Nullable WootFactoryMob<?> getMob() {
        if(mobTag == null)
            return null;
        return WootFactoryMobsRegistry.getFactoryMob(mobTag);
    }

    public int index;

    private int vitalityCost = 0;
    private int totalDrained = 0;
    private double perTickRatio = 0;
    private double accumulator = 0;
    private int numOfSim = 0;

    public int getNumberOfSimulations(){
        return numOfSim;
    }

    public int getVitalityCost(){
        return vitalityCost;
    }

    public int getTotalDrained(){
        return totalDrained;
    }

    public float getETA(){
        return (float)((vitalityCost - totalDrained - accumulator) / (perTickRatio * 20));
    }

    public float getRate(){
        return (float)perTickRatio;
    }

    public boolean setActive(int rate, int cost, int numOfSim){
        if(isDisabled())
            return false;

        this.perTickRatio = ((double)cost) / ((double)rate);
        this.accumulator = 0;
        this.vitalityCost = cost;
        this.totalDrained = 0;
        this.numOfSim = numOfSim;
        setChanged();
        return true;
    }

    public boolean isActive(){
        return totalDrained < vitalityCost;
    }

    public boolean tick(WootFluidTankHandler tank){
        if(!isActive())
            return false;

        if(redstoneMode != RedstoneMode.ONCE && isDisabled())
            return false;

        accumulator += perTickRatio;
        int drainAmount = (int) accumulator;
        accumulator -= drainAmount;

        if(drainAmount <= 0)
            return false;

        FluidStack simulate = tank.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);

        if(simulate.getAmount() < drainAmount){
            accumulator += (double)drainAmount - perTickRatio;
            setChanged();
            return false;
        }

        FluidStack consumed = tank.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
        totalDrained += consumed.getAmount();

        if(totalDrained >= vitalityCost){
            this.vitalityCost = 0;
            this.totalDrained = 0;
            setChanged();
            return true;
        }

        setChanged();
        return false;
    }

    public Tier getTier() {
        if (mobTag == null)
            return Tier.INVALID;

        if(!WootFactoryMobsRegistry.hasFactoryMob(mobTag))
            return Tier.INVALID;

        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(mobTag);

        return mob.getTier();
    }

    public static ItemStack getItemStack(CompoundTag tag) {
        ItemStack itemStack = BlocksRegistry.FAKE_SPAWNER_BLOCK.get().asItem().getDefaultInstance();

        CompoundTag blockTag = itemStack.getOrCreateTagElement("BlockEntityTag");
        blockTag.put(WootTags.MOB_TAG, tag);
        return itemStack;
    }

    protected RedstoneMode redstoneMode = RedstoneMode.ALWAYS_ON;

    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public void setRedstoneMode(RedstoneMode mode) {
        redstoneMode = mode;
    }

    private boolean lastRedstoneState = false;
    protected boolean isDisabled(){
        if(redstoneMode == RedstoneMode.ALWAYS_ON) return false;

        boolean current = level.hasNeighborSignal(getBlockPos());

        if(redstoneMode != RedstoneMode.ONCE)
            return (redstoneMode == RedstoneMode.WITH_SIGNAL) != current;

        boolean risingEdge = !lastRedstoneState && current;
        lastRedstoneState = current;
        return !risingEdge;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        tag.putInt(WootTags.REDSTONE_MODE_TAG, redstoneMode.ordinal());

        CompoundTag mobTag = getMobTag();
        if(mobTag != null)
            tag.put(WootTags.MOB_TAG, mobTag);

        tag.putInt(WootTags.Factory.NUMBER_OF_SIMULATIONS, numOfSim);
        tag.putInt(WootTags.Factory.VITALITY_COST, vitalityCost);
        tag.putInt(WootTags.Factory.TOTAL_DRAINED, totalDrained);
        tag.putDouble(WootTags.Factory.PER_TICK_RATIO, perTickRatio);
        tag.putDouble(WootTags.Factory.ACCUMULATOR, accumulator);
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        redstoneMode = RedstoneMode.byIndex(tag.getInt(WootTags.REDSTONE_MODE_TAG));

        if(tag.contains(WootTags.MOB_TAG))
            mobTag = tag.getCompound(WootTags.MOB_TAG);

        numOfSim = tag.getInt(WootTags.Factory.NUMBER_OF_SIMULATIONS);
        vitalityCost = tag.getInt(WootTags.Factory.VITALITY_COST);
        totalDrained = tag.getInt(WootTags.Factory.TOTAL_DRAINED);
        perTickRatio = tag.getDouble(WootTags.Factory.PER_TICK_RATIO);
        accumulator = tag.getDouble(WootTags.Factory.ACCUMULATOR);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag){
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(level == null || level.isClientSide) return;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void sendNewState(){
        PacketDistributor.SERVER.noArg().send(new WootFakeSpawnerUpdate(getBlockPos(), redstoneMode));
    }

    public void handleNewState(WootFakeSpawnerUpdate update){
        if(update.redstoneMode() != null)
            redstoneMode = update.redstoneMode();

        setChanged();
    }

    public boolean canPlayerAccess(ServerPlayer player) {
        return !(player.distanceToSqr(getBlockPos().getX() + 0.5,
                getBlockPos().getY() + 0.5,
                getBlockPos().getZ() + 0.5) > 64);
    }
}

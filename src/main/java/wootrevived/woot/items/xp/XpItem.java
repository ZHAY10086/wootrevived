package wootrevived.woot.items.xp;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.util.render.WootStyles;

import java.util.ArrayList;
import java.util.List;

public class XpItem extends Item {
    private static final int STACK_SIZE = 64;
    public static final int SPLINTERS_IN_STACK = 9;
    private static final int SHARD_XP = 9;
    private static final int SPLINTER_XP = 1;

    final Variant variant;
    public XpItem(Variant variant) {
        super(new Item.Properties().stacksTo(STACK_SIZE));
        this.variant = variant;
    }

    public Variant getVariant() { return this.variant; }

    public enum Variant {
        SHARD,
        SPLINTER
    }

    public static ItemStack getItemStack(Variant variant) {
        if (variant == Variant.SHARD) {
            return ItemsRegistry.XP_SHARD_ITEM.get().getDefaultInstance();
        }
        return ItemsRegistry.XP_SPLINTER_ITEM.get().getDefaultInstance();
    }

    public static List<ItemStack> getShards(int xp) {
        List<ItemStack> shards = new ArrayList<>();

        int xpShards = xp / SPLINTERS_IN_STACK;
        int xpSplinters = xp % SPLINTERS_IN_STACK;
        int fullStacks = xpShards / STACK_SIZE;
        int leftoverShard = xpShards % STACK_SIZE;

        for(int i = 0; i < fullStacks; i++){
            ItemStack itemStack = getItemStack(Variant.SHARD);
            itemStack.setCount(STACK_SIZE);
            shards.add(itemStack);
        }

        if(leftoverShard > 0){
            ItemStack itemStack = getItemStack(Variant.SHARD);
            itemStack.setCount(leftoverShard);
            shards.add(itemStack);
        }

        if (xpSplinters > 0) {
            ItemStack itemStack = getItemStack(Variant.SPLINTER);
            itemStack.setCount(xpSplinters);
            shards.add(itemStack);
        }

        return shards;
    }

    private int getXp(ItemStack itemStack) {
        if (itemStack.getItem() == ItemsRegistry.XP_SHARD_ITEM.get())
            return SHARD_XP;
        else if (itemStack.getItem() == ItemsRegistry.XP_SPLINTER_ITEM.get())
            return SPLINTER_XP;
        return 0;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand usedHand){
        if(level.isClientSide())
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        ItemStack itemStack = player.getItemInHand(usedHand);
        if(itemStack.isEmpty())
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        ItemStack advancementStack = itemStack.copy();

        level.playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                0.2F,
                0.5F * ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.8F));

        if(player instanceof FakePlayer){
            level.addFreshEntity(new ExperienceOrb(level, player.getX(), player.getY(), player.getZ(), 1));
            itemStack.shrink(1);
        } else {
            int xp = 0;
            if(player.isShiftKeyDown()){
                xp = getXp(itemStack) * itemStack.getCount();
                if(!player.isCreative())
                    itemStack.setCount(0);
            } else {
                xp = getXp(itemStack);
                if(!player.isCreative())
                    itemStack.shrink(1);
            }
            if(xp > 0){
                player.takeXpDelay = 0;
                ExperienceOrb orb = new ExperienceOrb(level, 0, 0, 0, xp);
                orb.playerTouch(player);
                player.takeXpDelay = 0;
                if(player instanceof ServerPlayer serverPlayer)
                    CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, advancementStack);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.translatable("info.woot_revived.shard.0").setStyle(WootStyles.DESCRIPTION_STYLE));
        tooltip.add(Component.translatable("info.woot_revived.shard.1").setStyle(WootStyles.DESCRIPTION_STYLE));
    }
}

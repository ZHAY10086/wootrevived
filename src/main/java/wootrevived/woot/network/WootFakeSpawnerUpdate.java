package wootrevived.woot.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.util.common.RedstoneMode;

public record WootFakeSpawnerUpdate(BlockPos blockPos, RedstoneMode redstoneMode) implements CustomPacketPayload {
    public static final ResourceLocation ID = Woot.location("woot_fake_spawner_upload");

    public static WootFakeSpawnerUpdate read(FriendlyByteBuf buf) {
        return new WootFakeSpawnerUpdate(buf.readBlockPos(), buf.readEnum(RedstoneMode.class));
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            Player sender = ctx.player().orElse(null);
            if (!(sender instanceof ServerPlayer player)) return;
            if (!player.level().isLoaded(blockPos)) return;

            BlockEntity blockEntity = sender.level().getBlockEntity(blockPos);
            if (blockEntity instanceof FakeSpawnerBlockEntity fakeSpawnerBlockEntity && fakeSpawnerBlockEntity.canPlayerAccess(player)) {
                fakeSpawnerBlockEntity.handleNewState(this);
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeEnum(redstoneMode);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

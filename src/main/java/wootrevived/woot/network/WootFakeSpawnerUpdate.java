package wootrevived.woot.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.util.common.RedstoneMode;

import java.util.function.Supplier;

public record WootFakeSpawnerUpdate(BlockPos blockPos, RedstoneMode redstoneMode) {
    public static WootFakeSpawnerUpdate decode(FriendlyByteBuf buf) {
        return new WootFakeSpawnerUpdate(buf.readBlockPos(), buf.readEnum(RedstoneMode.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeEnum(redstoneMode);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null || sender.level() == null) return;
            if (!sender.level().isLoaded(blockPos)) return;

            BlockEntity blockEntity = sender.level().getBlockEntity(blockPos);
            if (blockEntity instanceof FakeSpawnerBlockEntity fakeSpawnerBlockEntity && fakeSpawnerBlockEntity.canPlayerAccess(sender)) {
                fakeSpawnerBlockEntity.handleNewState(this);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

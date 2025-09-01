package wootrevived.woot.network;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wootrevived.woot.Woot;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record WootMachineUpdate(BlockPos blockPos, RedstoneMode redstoneMode,
                                List<Map<MachineSide, MachineSideProperty>> listMachineProperties) implements CustomPacketPayload {
    public static final ResourceLocation ID = Woot.location("woot_machine_update");

    public static WootMachineUpdate read(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Map<MachineSide, MachineSideProperty>> listMachineProperties = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<MachineSide, MachineSideProperty> machineSideProperties = Maps.newEnumMap(MachineSide.class);
            for (int j = 0; j < MachineSide.values().length; j++) {
                machineSideProperties.put(buf.readEnum(MachineSide.class), buf.readEnum(MachineSideProperty.class));
            }
            listMachineProperties.add(machineSideProperties);
        }
        return new WootMachineUpdate(buf.readBlockPos(), buf.readEnum(RedstoneMode.class), listMachineProperties);
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            Player sender = ctx.player().orElse(null);
            if (!(sender instanceof ServerPlayer player)) return;
            if (!player.level().isLoaded(blockPos)) return;

            BlockEntity blockEntity = player.level().getBlockEntity(blockPos);
            if (blockEntity instanceof WootMachineBlockEntity wootMachineBlockEntity && wootMachineBlockEntity.canPlayerAccess(player)) {
                wootMachineBlockEntity.handleNewState(this);
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(listMachineProperties.size());
        for (Map<MachineSide, MachineSideProperty> machineProperties : listMachineProperties) {
            for (Map.Entry<MachineSide, MachineSideProperty> entry : machineProperties.entrySet()) {
                buf.writeEnum(entry.getKey());
                buf.writeEnum(entry.getValue());
            }
        }
        buf.writeBlockPos(blockPos);
        buf.writeEnum(redstoneMode);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

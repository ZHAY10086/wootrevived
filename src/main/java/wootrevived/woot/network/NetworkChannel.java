package wootrevived.woot.network;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import wootrevived.woot.Woot;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.Optional;

public class NetworkChannel {

    private static final ResourceLocation resourceLocation = ResourceLocation.tryBuild(Woot.MOD_ID, "net");

    public static void init(){}

    public static SimpleChannel channel;
    static {
        channel = NetworkRegistry.ChannelBuilder.named(resourceLocation)
                .clientAcceptedVersions(s -> Objects.equals(s, "1"))
                .serverAcceptedVersions(s -> Objects.equals(s, "1"))
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

        channel.registerMessage(
                0,
                WootMachineUpdate.class,
                WootMachineUpdate::encode,
                WootMachineUpdate::decode,
                WootMachineUpdate::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );

        channel.registerMessage(
                1,
                WootFakeSpawnerUpdate.class,
                WootFakeSpawnerUpdate::encode,
                WootFakeSpawnerUpdate::decode,
                WootFakeSpawnerUpdate::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}

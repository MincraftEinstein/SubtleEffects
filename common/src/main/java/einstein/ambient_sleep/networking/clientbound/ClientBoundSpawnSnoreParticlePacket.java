package einstein.ambient_sleep.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ClientBoundSpawnSnoreParticlePacket {

    public static final ResourceLocation CHANNEL = AmbientSleep.loc("spawn_snore_particle");

    private final double x;
    private final double y;
    private final double z;

    public ClientBoundSpawnSnoreParticlePacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static ClientBoundSpawnSnoreParticlePacket decode(FriendlyByteBuf buf) {
        return new ClientBoundSpawnSnoreParticlePacket(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static void handle(PacketContext<ClientBoundSpawnSnoreParticlePacket> context) {
        Level level = Minecraft.getInstance().level;
        if (context.side().equals(Side.CLIENT) && level != null) {
            ClientBoundSpawnSnoreParticlePacket packet = context.message();
            if (ModConfigs.INSTANCE.enableSleepingZs.get() && ModConfigs.INSTANCE.beehivesHaveSleepingZs.get()) {
                level.addParticle(ModParticles.SNORING.get(), packet.x, packet.y, packet.z, 0, 0, 0);
            }
        }
    }
}

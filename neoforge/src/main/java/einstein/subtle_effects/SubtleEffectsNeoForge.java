package einstein.subtle_effects;

import einstein.subtle_effects.platform.NeoForgeNetworkHelper;
import einstein.subtle_effects.platform.NeoForgeRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

@Mod(MOD_ID)
public class SubtleEffectsNeoForge {

    public SubtleEffectsNeoForge(IEventBus modEventBus) {
        SubtleEffects.init();
        NeoForgeRegistryHelper.PARTICLE_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener((RegisterPayloadHandlersEvent event) -> {
            PayloadRegistrar registrar = event.registrar("1").optional();
            NeoForgeNetworkHelper.PAYLOAD_DATA.forEach((type, payloadData) -> register(registrar, type, payloadData));
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void register(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, NeoForgeNetworkHelper.PayloadData<?> data) {
        NeoForgeNetworkHelper.PayloadData<T> payloadData = (NeoForgeNetworkHelper.PayloadData<T>) data;
        registrar.playToClient(type, payloadData.streamCodec, (packet, context) -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                payloadData.handler.accept(level, packet);
            }
        });
    }
}
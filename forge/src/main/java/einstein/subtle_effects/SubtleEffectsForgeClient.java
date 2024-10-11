package einstein.subtle_effects;

import einstein.subtle_effects.platform.ForgeNetworkHelper;
import einstein.subtle_effects.platform.ForgeRegistryHelper;
import einstein.subtle_effects.platform.services.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(value = SubtleEffects.MOD_ID)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class SubtleEffectsForgeClient {

    public SubtleEffectsForgeClient(IEventBus modEventBus) {
        SubtleEffectsClient.clientSetup();
        ForgeNetworkHelper.init(NetworkHelper.Direction.TO_SERVER);
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                ForgeRegistryHelper.PARTICLE_PROVIDERS.forEach((particle, provider) -> registerParticle(event, particle, provider))
        );
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            SubtleEffectsClient.clientTick(minecraft, minecraft.level);
        });
        MinecraftForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> SubtleEffectsClient.registerClientCommands(event.getDispatcher(), event.getBuildContext()));
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParticleType<V>, V extends ParticleOptions> void registerParticle(RegisterParticleProvidersEvent event, Supplier<? extends ParticleType<?>> particle, Function<SpriteSet, ? extends ParticleProvider<?>> provider) {
        Supplier<T> t = (Supplier<T>) particle;
        Function<SpriteSet, V> v = (Function<SpriteSet, V>) provider;
        event.registerSpriteSet(t.get(), sprites -> (ParticleProvider<V>) v.apply(sprites));
    }
}

package einstein.subtle_effects;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.platform.NeoForgeRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

@Mod(MOD_ID)
public class SubtleEffectsNeoForge {

    public SubtleEffectsNeoForge(IEventBus modEventBus, ModContainer container) {
        SubtleEffects.init();
        NeoForgeRegistryHelper.PARTICLE_TYPES.register(modEventBus);
        NeoForgeRegistryHelper.SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                NeoForgeRegistryHelper.PARTICLE_PROVIDERS.forEach((particle, provider) -> registerParticle(event, particle, provider))
        );
        modEventBus.addListener((FMLClientSetupEvent event) -> SubtleEffectsClient.clientSetup());
        modEventBus.addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getModId().equals(MOD_ID)) {
                SubtleEffectsClient.configReloaded();
            }
        });
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            SubtleEffectsClient.clientTick(minecraft, minecraft.level);
        });
        container.registerConfig(ModConfig.Type.CLIENT, ModConfigs.SPEC);
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParticleType<V>, V extends ParticleOptions> void registerParticle(RegisterParticleProvidersEvent event, Supplier<? extends ParticleType<?>> particle, Function<SpriteSet, ? extends ParticleProvider<?>> provider) {
        Supplier<T> t = (Supplier<T>) particle;
        Function<SpriteSet, V> v = (Function<SpriteSet, V>) provider;
        event.registerSpriteSet(t.get(), sprites -> (ParticleProvider<V>) v.apply(sprites));
    }
}
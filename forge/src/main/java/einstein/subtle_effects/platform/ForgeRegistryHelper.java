package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.RegistryHelper;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;
import static einstein.subtle_effects.SubtleEffects.loc;

public class ForgeRegistryHelper implements RegistryHelper {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
    public static final Map<Supplier<? extends ParticleType<?>>, Function<SpriteSet, ? extends ParticleProvider<?>>> PARTICLE_PROVIDERS = new HashMap<>();

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        return PARTICLE_TYPES.register(name, particle);
    }

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particle, Function<SpriteSet, ParticleProvider<V>> provider) {
        PARTICLE_PROVIDERS.put(particle, provider);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(loc(name)));
    }
}

package einstein.subtle_effects.platform.services;

import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public interface RegistryHelper {

    <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle);

    <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particle, Function<SpriteSet, ParticleProvider<V>> provider);

    void registerParticleGroup(ParticleRenderType group, Function<ParticleEngine, ParticleGroup<?>> factory);

    Supplier<SoundEvent> registerSound(String name);
}

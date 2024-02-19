package einstein.ambient_sleep.platform.services;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public interface RegistryHelper {

    <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle);

    <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particle, Function<SpriteSet, ParticleProvider<V>> provider);

    Supplier<SoundEvent> registerSound(String name);
}

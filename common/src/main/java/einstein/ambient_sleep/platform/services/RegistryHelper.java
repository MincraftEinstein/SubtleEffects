package einstein.ambient_sleep.platform.services;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public interface RegistryHelper {

    <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle);

    void registerParticleProvider(Supplier<SimpleParticleType> particle, Function<SpriteSet, ParticleProvider<SimpleParticleType>> provider);

    Supplier<SoundEvent> registerSound(String name);
}

package einstein.ambient_sleep.platform.services;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public interface RegistryHelper {

    <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle);

    Supplier<SoundEvent> registerSound(String name);
}

package einstein.ambient_sleep.platform;

import einstein.ambient_sleep.platform.services.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

import static einstein.ambient_sleep.AmbientSleep.loc;

public class FabricRegistryHelper implements RegistryHelper {

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        T t = Registry.register(BuiltInRegistries.PARTICLE_TYPE, loc(name), particle.get());
        return () -> t;
    }
}

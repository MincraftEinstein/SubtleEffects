package einstein.ambient_sleep.platform;

import einstein.ambient_sleep.platform.services.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

import static einstein.ambient_sleep.AmbientSleep.loc;

public class FabricRegistryHelper implements RegistryHelper {

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        T t = Registry.register(BuiltInRegistries.PARTICLE_TYPE, loc(name), particle.get());
        return () -> t;
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        ResourceLocation location = loc(name);
        SoundEvent sound = Registry.register(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
        return () -> sound;
    }
}

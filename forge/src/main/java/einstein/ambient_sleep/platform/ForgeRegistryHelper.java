package einstein.ambient_sleep.platform;

import einstein.ambient_sleep.platform.services.RegistryHelper;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.ambient_sleep.AmbientSleep.MOD_ID;
import static einstein.ambient_sleep.AmbientSleep.loc;

public class ForgeRegistryHelper implements RegistryHelper {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
    public static final Map<Supplier<SimpleParticleType>, Function<SpriteSet, ParticleProvider<SimpleParticleType>>> PARTICLE_PROVIDERS = new HashMap<>();

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        return PARTICLE_TYPES.register(name, particle);
    }

    @Override
    public void registerParticleProvider(Supplier<SimpleParticleType> particle, Function<SpriteSet, ParticleProvider<SimpleParticleType>> provider) {
        PARTICLE_PROVIDERS.put(particle, provider);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(loc(name)));
    }
}

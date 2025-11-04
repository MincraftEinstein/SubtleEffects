package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.RegistryHelper;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;
import static einstein.subtle_effects.SubtleEffects.loc;

public class NeoForgeRegistryHelper implements RegistryHelper {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);
    public static final Map<Supplier<? extends ParticleType<?>>, Function<SpriteSet, ? extends ParticleProvider<?>>> PARTICLE_PROVIDERS = new HashMap<>();
    public static final Map<ParticleRenderType, Function<ParticleEngine, ParticleGroup<?>>> PARTICLE_GROUP_FACTORIES = new HashMap<>();

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        return PARTICLE_TYPES.register(name, particle);
    }

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particle, Function<SpriteSet, ParticleProvider<V>> provider) {
        PARTICLE_PROVIDERS.put(particle, provider);
    }

    @Override
    public void registerParticleGroup(ParticleRenderType group, Function<ParticleEngine, ParticleGroup<?>> factory) {
        PARTICLE_GROUP_FACTORIES.put(group, factory);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(loc(name)));
    }
}

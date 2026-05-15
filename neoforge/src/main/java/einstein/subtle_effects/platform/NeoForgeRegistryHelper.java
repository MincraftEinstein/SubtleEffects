package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.RegistryHelper;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;
import static einstein.subtle_effects.SubtleEffects.loc;

public class NeoForgeRegistryHelper implements RegistryHelper {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        return PARTICLE_TYPES.register(name, particle);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(loc(name)));
    }
}

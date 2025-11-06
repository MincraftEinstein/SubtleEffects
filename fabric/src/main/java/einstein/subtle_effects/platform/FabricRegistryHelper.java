package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.RegistryHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRendererRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.SubtleEffects.loc;

public class FabricRegistryHelper implements RegistryHelper {

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        T t = Registry.register(BuiltInRegistries.PARTICLE_TYPE, loc(name), particle.get());
        return () -> t;
    }

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particle, Function<SpriteSet, ParticleProvider<V>> provider) {
        ParticleFactoryRegistry.getInstance().register(particle.get(), provider::apply);
    }

    @Override
    public void registerParticleGroup(ParticleRenderType group, Function<ParticleEngine, ParticleGroup<?>> factory) {
        ParticleRendererRegistry.register(group, factory);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        ResourceLocation location = loc(name);
        SoundEvent sound = Registry.register(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
        return () -> sound;
    }
}

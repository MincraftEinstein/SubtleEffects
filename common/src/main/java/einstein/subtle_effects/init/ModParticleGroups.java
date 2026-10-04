package einstein.subtle_effects.init;

import einstein.subtle_effects.particle.group.ModelParticleGroup;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;

import java.util.function.Function;

import static einstein.subtle_effects.SubtleEffects.loc;

public class ModParticleGroups {

    public static final ParticleRenderType MODEL = register("model", "SE_M",ModelParticleGroup::new);

    public static void init() {
    }

    private static ParticleRenderType register(String name, String shorthand, Function<ParticleEngine, ParticleGroup<?>> factory) {
        ParticleRenderType group = new ParticleRenderType(loc(name).toString(), shorthand);
        Services.PARTICLE_HELPER.registerParticleGroup(group, factory);
        return group;
    }

}

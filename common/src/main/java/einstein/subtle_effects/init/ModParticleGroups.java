package einstein.subtle_effects.init;

import einstein.subtle_effects.particle.group.ModelParticleGroup;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;

import java.util.function.Function;

import static einstein.subtle_effects.SubtleEffects.loc;
import static einstein.subtle_effects.platform.Services.REGISTRY;

public class ModParticleGroups {

    public static final ParticleRenderType MODEL = register("model", "SE_M",ModelParticleGroup::new);

    public static void init() {
    }

    private static ParticleRenderType register(String name, String shorthand, Function<ParticleEngine, ParticleGroup<?>> factory) {
        var group = new ParticleRenderType(loc(name).toString(), shorthand);
        REGISTRY.registerParticleGroup(group, factory);
        return group;
    }

}

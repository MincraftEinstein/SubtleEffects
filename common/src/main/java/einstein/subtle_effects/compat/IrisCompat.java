package einstein.subtle_effects.compat;

import einstein.subtle_effects.init.ModPipelines;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisProgram;

public class IrisCompat {

    public static void init() {
        IrisApi.getInstance().assignPipeline(ModPipelines.CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE, IrisProgram.PARTICLES_TRANSLUCENT);
        IrisApi.getInstance().assignPipeline(ModPipelines.BLENDED_PIPELINE, IrisProgram.PARTICLES_TRANSLUCENT);
    }
}

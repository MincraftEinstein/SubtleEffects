package einstein.subtle_effects.compat;

import einstein.subtle_effects.init.ModParticleLayers;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisProgram;

public class IrisCompat {

    public static void init() {
        IrisApi.getInstance().assignPipeline(ModParticleLayers.BLENDED_PIPELINE, IrisProgram.PARTICLES_TRANSLUCENT);
    }
}

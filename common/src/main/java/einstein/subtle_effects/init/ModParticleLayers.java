package einstein.subtle_effects.init;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;

import static einstein.subtle_effects.init.ModPipelines.BLENDED_PIPELINE;

public class ModParticleLayers {

    @SuppressWarnings("deprecation")
    public static final SingleQuadParticle.Layer BLENDED = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, BLENDED_PIPELINE);

    public static void init() {
    }

    public static SingleQuadParticle.Layer getBlendedOrTransparent() {
        if (ModConfigs.GENERAL.allowUsingBlendedRenderType.get()) {
            return BLENDED;
        }
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }
}

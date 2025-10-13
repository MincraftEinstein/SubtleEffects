package einstein.subtle_effects.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class ModParticleLayers {

    private static final BlendFunction ALPHA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
    public static final RenderPipeline BLENDED_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/subtle_effects_fog")
                    .withBlend(ALPHA_BLEND)
                    .withDepthWrite(false)
                    .build()
    );

    public static final SingleQuadParticle.Layer BLENDED = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, BLENDED_PIPELINE);

    public static void init() {
    }

    public static SingleQuadParticle.Layer getBlendedOrTransparent() {
        if (ModConfigs.GENERAL.allowUsingBlendedRenderType) {
            return BLENDED;
        }
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }
}

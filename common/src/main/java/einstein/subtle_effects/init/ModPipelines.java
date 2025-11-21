package einstein.subtle_effects.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.renderer.RenderPipelines;

import static einstein.subtle_effects.SubtleEffects.loc;


public class ModPipelines {

    private static final BlendFunction ALPHA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final RenderPipeline CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation(loc("pipeline/entity_particle_translucent"))
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build());

    public static final RenderPipeline BLENDED_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation(loc("pipeline/alpha_blend"))
                    .withBlend(ALPHA_BLEND)
                    .withDepthWrite(false)
                    .build()
    );

    public static void init() {
    }
}

package einstein.subtle_effects.init;

import com.mojang.renderpearl.api.pipeline.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.oit.OitPipelineSet;

import static einstein.subtle_effects.SubtleEffects.loc;


public class ModPipelines {

    private static final BlendFunction ALPHA_BLEND = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

    public static final RenderPipeline CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation(loc("pipeline/entity_particle_translucent"))
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withCull(false)
                    .build());

    public static final RenderPipeline BLENDED_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation(loc("pipeline/alpha_blend"))
                    .withColorTargetState(new ColorTargetState(ALPHA_BLEND))
                    .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
                    .build()
    );

    public static final OitPipelineSet OIT_PARTICLE = RenderPipelines.register(
            oitPipeline(RenderPipelines.OIT_PARTICLE_SNIPPET, "particle")
    );

    public static void init() {
    }

    public static OitPipelineSet oitPipeline(RenderPipeline.Snippet baseSnippet, String locationSuffix) {
        RenderPipeline.Builder depthBoundsBuilder = RenderPipeline.builder(baseSnippet, RenderPipelines.OIT_DEPTH_BOUNDS_SNIPPET)
                .withCull(false)
                .withLocation(loc("pipeline/oit_depth_bounds_" + locationSuffix));
        RenderPipeline.Builder transmittanceBuilder = RenderPipeline.builder(baseSnippet, RenderPipelines.OIT_TRANSMITTANCE_SNIPPET)
                .withCull(false)
                .withLocation(loc("pipeline/oit_transmittance_" + locationSuffix));
        RenderPipeline.Builder accumulateBuilder = RenderPipeline.builder(baseSnippet, RenderPipelines.OIT_ACCUMULATE_SNIPPET)
                .withCull(false)
                .withLocation(loc("pipeline/oit_accumulate_" + locationSuffix));
        return new OitPipelineSet(depthBoundsBuilder.build(), transmittanceBuilder.build(), accumulateBuilder.build());
    }

}
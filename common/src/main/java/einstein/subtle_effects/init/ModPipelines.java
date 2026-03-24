package einstein.subtle_effects.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.renderer.RenderPipelines;

import static einstein.subtle_effects.SubtleEffects.loc;


public class ModPipelines {

    private static final BlendFunction ALPHA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

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
                    .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                    .build()
    );

    public static void init() {
    }
}

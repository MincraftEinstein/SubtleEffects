package einstein.subtle_effects.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

import static einstein.subtle_effects.SubtleEffects.loc;


public class ModRenderTypes {

    public static final RenderPipeline ENTITY_PARTICLE_TRANSLUCENT_RENDER_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder()
                    .withVertexShader("core/particle")
                    .withFragmentShader("core/particle")
                    .withSampler("Sampler0")
                    .withSampler("Sampler2")
                    .withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
                    .withLocation(loc("pipeline/entity_particle_translucent"))
                    .withoutBlend()
                    .withCull(false)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .build());

    public static final RenderPipeline TRANSLUCENT_PARTICLE_2 = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/translucent_particle_2")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .build()
    );

    public static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE = Util.memoize((texture, outline) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false))
                .setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(outline);
        var pipeline = (!false) ? RenderPipelines.ENTITY_TRANSLUCENT : ENTITY_PARTICLE_TRANSLUCENT_RENDER_PIPELINE;
        return RenderType.create("subtle_effects:entity_particle_translucent", 1536, true, true, pipeline, state);
    });

    public static RenderType entityParticleTranslucent(ResourceLocation texture) {
        return ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE.apply(texture, false);
    }

    public static void init() {
    }
}

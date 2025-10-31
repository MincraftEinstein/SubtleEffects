package einstein.subtle_effects.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;


public class ModRenderTypes {

    public static final RenderPipeline ENTITY_PARTICLE_TRANSLUCENT_RENDER_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/subtle_effects_entity_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build());


    public static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE = Util.memoize((texture, outline) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false))
                .setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(outline);
        return RenderType.create("subtle_effects_entity_particle_translucent", 1536, true, true, RenderPipelines.ENTITY_TRANSLUCENT, compositeState);
    });

    public static RenderType entityParticleTranslucent(ResourceLocation texture) {
        return ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE.apply(texture, false);
    }

    public static void init() {
    }
}

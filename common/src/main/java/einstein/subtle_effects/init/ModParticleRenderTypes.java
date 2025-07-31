package einstein.subtle_effects.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.TriState;

import java.util.ArrayList;
import java.util.List;

public class ModParticleRenderTypes {

    private static final BlendFunction ALPHA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
    private static final RenderPipeline BLENDED_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/subtle_effects_fog")
                    .withBlend(ALPHA_BLEND)
                    .withDepthWrite(false)
                    .build()
    );
    private static final RenderType BLENDED_RENDER_TYPE = RenderType.create(
            "subtle_effects_fog",
            1536,
            BLENDED_PIPELINE,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, TriState.FALSE, false))
                    .setOutputState(RenderStateShard.PARTICLES_TARGET)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .createCompositeState(false)
    );
    public static final ParticleRenderType BLENDED = new ParticleRenderType("subtle_effects:blended", BLENDED_RENDER_TYPE);

    public static void init() {
        List<ParticleRenderType> renderTypes = new ArrayList<>(ParticleEngineAccessor.getRenderOrder());
        renderTypes.add(BLENDED);
        ParticleEngineAccessor.setRenderOrder(List.copyOf(renderTypes));
    }
}

package einstein.subtle_effects.init;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

import static einstein.subtle_effects.init.ModPipelines.CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE;


public class ModRenderTypes {

    public static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE = Util.memoize((texture, outline) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false))
                .setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(outline);
        return RenderType.create("subtle_effects:entity_particle_translucent", 1536, true, true, CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE, state);
    });

    public static RenderType entityParticleTranslucent(ResourceLocation texture) {
        return ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE.apply(texture, false);
    }

    public static void init() {
    }
}

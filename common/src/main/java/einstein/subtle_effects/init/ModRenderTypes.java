package einstein.subtle_effects.init;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

import static einstein.subtle_effects.init.ModPipelines.CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE;


public class ModRenderTypes {

    public static final Function<Identifier, RenderType> ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE = Util.memoize((texture) -> {
        var builder = RenderSetup.builder(CUSTOM_TRANSLUCENT_PARTICLE_PIPELINE)
                .withTexture("Sampler0", texture)
                .useLightmap()
                .useOverlay()
                .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                .createRenderSetup();
        return RenderType.create("subtle_effects:entity_particle_translucent", builder);
    });

    public static RenderType entityParticleTranslucent(Identifier texture) {
        return ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE.apply(texture);
    }

    public static void init() {
    }
}

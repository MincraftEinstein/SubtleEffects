package einstein.subtle_effects.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class ModRenderTypes {

    public static final RenderStateShard.ShaderStateShard RENDER_TYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getParticleShader);

    public static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE = Util.memoize((texture, outline) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(RENDER_TYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderType.NO_CULL)
                .setOverlayState(RenderType.OVERLAY)
                .createCompositeState(outline);
        return RenderType.create("subtle_effects:entity_particle_translucent", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 1536, true, true, compositeState);
    });

    public static void init() {
    }
}

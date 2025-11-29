package einstein.subtle_effects.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class ModRenderTypes {

    public static final ResourceLocation RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER_ID = SubtleEffects.loc("rendertype_entity_particle_translucent");
    public static ShaderInstance RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER;
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER_STATE = new RenderStateShard.ShaderStateShard(() -> RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER);

    public static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_PARTICLE_TRANSLUCENT_RENDER_TYPE = Util.memoize((texture, outline) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_PARTICLE_TRANSLUCENT_SHADER_STATE)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(outline);
        return RenderType.create("subtle_effects:entity_particle_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    });

    public static void init() {
    }
}

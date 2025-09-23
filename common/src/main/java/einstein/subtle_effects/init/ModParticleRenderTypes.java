package einstein.subtle_effects.init;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import einstein.subtle_effects.mixin.client.particle.ParticleEngineAccessor;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

public class ModParticleRenderTypes {

    public static final ParticleRenderType BLENDED = (tesselator, textureManager) -> {
        RenderSystem.depthMask(false);

        // noinspection deprecation
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    };

    public static void init() {
        List<ParticleRenderType> renderTypes = new ArrayList<>(ParticleEngineAccessor.getRenderOrder());
        renderTypes.add(BLENDED);
        ParticleEngineAccessor.setRenderOrder(List.copyOf(renderTypes));
    }

    public static ParticleRenderType getBlendedOrTransparent() {
        if (ModConfigs.GENERAL.allowUsingBlendedRenderType) {
            return BLENDED;
        }
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

package einstein.subtle_effects.mixin.client.particle.biome_water_colors;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.BubbleParticleOverlayHandler;
import einstein.subtle_effects.util.SpriteSetSetter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({BubbleParticle.class, BubbleColumnUpParticle.class})
public abstract class BubbleParticleMixin extends TextureSheetParticle implements SpriteSetSetter {

    @Unique
    private final BubbleParticleOverlayHandler subtleEffects$handler = new BubbleParticleOverlayHandler(this);

    protected BubbleParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        super.renderRotatedQuad(buffer, quaternion, x, y, z, partialTicks);
        subtleEffects$handler.render(buffer, quaternion, x, y, z, partialTicks);
    }

    @Override
    public void subtleEffects$setSpriteSet(SpriteSet sprites) {
        subtleEffects$handler.setSprites(sprites);
    }
}

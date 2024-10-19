package einstein.subtle_effects.mixin.client.particle.biome_water_colors;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.BubbleParticleOverlayHandler;
import einstein.subtle_effects.util.SpriteSetSetter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.WaterCurrentDownParticle;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WaterCurrentDownParticle.class)
public abstract class WaterCurrentDownParticleMixin extends TextureSheetParticle implements SpriteSetSetter {

    @Unique
    private final BubbleParticleOverlayHandler subtleEffects$handler = new BubbleParticleOverlayHandler(this);

    protected WaterCurrentDownParticleMixin(ClientLevel level, double x, double y, double z) {
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

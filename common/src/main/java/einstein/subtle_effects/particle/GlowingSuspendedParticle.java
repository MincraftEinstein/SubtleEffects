package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class GlowingSuspendedParticle extends SuspendedParticle {

    public GlowingSuspendedParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }
}

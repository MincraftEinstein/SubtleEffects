package einstein.subtle_effects.particle;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedParticle;

public class GlowingSuspendedParticle extends SuspendedParticle {

    public GlowingSuspendedParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.getLightColor(super.getLightColor(partialTick));
    }
}

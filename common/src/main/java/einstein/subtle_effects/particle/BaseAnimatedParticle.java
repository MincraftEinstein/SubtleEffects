package einstein.subtle_effects.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class BaseAnimatedParticle extends TextureSheetParticle {

    private final ParticleAnimation animation;

    public BaseAnimatedParticle(ClientLevel level, double x, double y, double z, ParticleAnimation animation) {
        super(level, x, y, z);
        this.animation = animation;
        lifetime = animation.getAnimationLifetime();
        updateSprite();
    }

    protected BaseAnimatedParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleAnimation animation) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.animation = animation;
        lifetime = animation.getAnimationLifetime();
        updateSprite();
    }

    @Override
    public void tick() {
        super.tick();
        if (!removed) {
            animation.tick();
            updateSprite();
        }
    }

    private void updateSprite() {
        if (!removed) {
            setSprite(animation.getSpriteForFrame());
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}

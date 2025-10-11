package com.mincrafteinstein.subtleeffects.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * A soft drifting flake particle triggered when brushing an Armadillo.
 */
public class BrushFlakeParticle extends TextureSheetParticle {
    protected BrushFlakeParticle(ClientLevel level, double x, double y, double z,
                                 double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.quadSize = 0.05F + random.nextFloat() * 0.05F;
        this.lifetime = 20 + random.nextInt(10);
        this.gravity = 0.01F;
        this.alpha = 0.9F;
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.xd *= 0.96;
        this.zd *= 0.96;
        if (this.age > this.lifetime * 0.7F) this.alpha *= 0.95F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Factory(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z, double vx, double vy, double vz) {
            return new BrushFlakeParticle(level, x, y, z, vx, vy, vz, sprites);
        }
    }
}

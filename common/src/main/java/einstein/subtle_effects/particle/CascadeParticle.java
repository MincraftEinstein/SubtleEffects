package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;

public class CascadeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final BlockPos.MutableBlockPos pos;
    private final LifetimeAlpha lifetimeAlpha = new LifetimeAlpha(0.7F, 0, 0.5F, 1);

    protected CascadeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, 0, zSpeed);
        this.sprites = sprites;
        pos = BlockPos.containing(x, y, z).mutable();
        gravity = 0.3F;
        speedUpWhenYMotionIsBlocked = true;
        alpha = lifetimeAlpha.startAlpha();
        scale(3);
        setSpriteFromAge(sprites);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        super.render(consumer, camera, partialTicks);
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        pos.set(x, y, z);

        if (onGround) {
            remove();
            return;
        }

        if (!level.getFluidState(pos).isEmpty()) {
            remove();
            return;
        }

        setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CascadeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}

package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModParticleRenderTypes;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class SlimeTrailParticle extends FlatPlaneParticle {

    private final BlockPos pos;

    protected SlimeTrailParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, float scale) {
        super(level, x, y, z);
        pickSprite(sprites);
        quadSize = 0.5F * scale;
        setSize(quadSize + 1, 0.1F);
        lifetime = (int) Math.min(300 + (200 * scale), 1200);
        rotation.rotateY(90 * random.nextInt(3) * Mth.DEG_TO_RAD).rotateX(-90 * Mth.DEG_TO_RAD);
        pos = new BlockPos.MutableBlockPos(x, y, z);
        renderBackFace = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.BLENDED;
    }

    @Override
    public void tick() {
        if (!level.getBlockState(pos).isAir() || level.getBlockState(pos.below()).isAir()) {
            remove();
            return;
        }

        if (age >= (lifetime / 2)) {
            alpha = Mth.clamp(alpha - 0.015F, 0, 1);

            if (alpha == 0) {
                remove();
            }
        }

        if (age++ >= lifetime) {
            remove();
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SlimeTrailParticle(level, sprites, x, y, z, Math.min(options.f(), 64));
        }
    }
}

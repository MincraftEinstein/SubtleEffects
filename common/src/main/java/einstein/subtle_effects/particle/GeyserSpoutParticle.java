package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.IntegerParticleOptions;
import einstein.subtle_effects.tickers.FlameGeyserTicker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class GeyserSpoutParticle extends FlatPlaneParticle {

    private static final int FADE_TIME = 60;
    private static final float START_ALPHA = 1F;
    private final int geyserLifeTime;
    private final BlockPos pos = BlockPos.containing(x, y, z);

    protected GeyserSpoutParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, int geyserLifeTime) {
        super(level, x, y, z);
        this.geyserLifeTime = geyserLifeTime;
        rotation.rotateY(90 * random.nextInt(3) * Mth.DEG_TO_RAD).rotateX(90 * Mth.DEG_TO_RAD);
        lifetime = geyserLifeTime + FADE_TIME;
        hasPhysics = false;
        renderBackFace = false;
        alpha = START_ALPHA;
        quadSize = 0.5F;
        setSize(1.5F, 0.1F);
        pickSprite(sprites);
    }

    @Override
    public void tick() {
        if (!FlameGeyserTicker.isNotFaceSturdyOrFluidEmpty(level, pos) || !FlameGeyserTicker.VALID_BLOCKS.contains(level.getBlockState(pos.below()).getBlock())) {
            remove();
            return;
        }

        if (age >= geyserLifeTime) {
            alpha -= START_ALPHA / FADE_TIME;
        }

        if (age++ >= lifetime) {
            remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<IntegerParticleOptions> {

        @Override
        public Particle createParticle(IntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GeyserSpoutParticle(level, x, y, z, sprites, options.integer());
        }
    }
}

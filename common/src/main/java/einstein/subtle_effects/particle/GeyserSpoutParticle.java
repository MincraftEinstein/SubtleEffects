package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.GeyserSpoutParticleOptions;
import einstein.subtle_effects.ticking.tickers.geyser.FlameGeyserTicker;
import einstein.subtle_effects.ticking.tickers.geyser.GeyserType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class GeyserSpoutParticle extends FlatPlaneParticle {

    private static final int FADE_TIME = 60;
    private static final float START_ALPHA = 1F;
    private final int geyserLifeTime;
    private final GeyserType type;
    private final BlockPos pos = BlockPos.containing(x, y, z);

    protected GeyserSpoutParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, GeyserSpoutParticleOptions options) {
        super(level, x, y, z);
        geyserLifeTime = options.lifeTime();
        type = options.type();
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
        if (!FlameGeyserTicker.isNotFaceSturdyOrFluidEmpty(type, level, pos) || !type.getSpawnableBlocks().contains(level.getBlockState(pos.below()).getBlock())) {
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
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<GeyserSpoutParticleOptions> {

        @Override
        public Particle createParticle(GeyserSpoutParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GeyserSpoutParticle(level, x, y, z, sprites, options);
        }
    }
}

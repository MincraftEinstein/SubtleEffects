package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.DirectionParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import static einstein.subtle_effects.util.Util.radians;

public class EggSplatParticle extends FlatPlaneParticle {

    private final Direction direction;

    protected EggSplatParticle(ClientLevel level, double x, double y, double z, Direction direction, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.direction = direction;
        rotation = direction.getRotation().rotateX(radians(-90));
        lifetime = 120;
        quadSize = 0.2F;

        if (direction.getAxis().isVertical()) {
            rotation.rotateZ(radians(90 * random.nextInt(3)));
        }
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos pos = BlockPos.containing(x, y, z);
        if (level.getBlockState(pos.relative(direction)).isAir()) {
            remove();
            return;
        }

        if (Util.isSolidOrNotEmpty(level, pos)) {
            remove();
            return;
        }

        int i = (lifetime / 3) * 2;
        if (age == i) {
            if (direction.getAxis().isHorizontal()) {
                gravity = 0.05F;
            }
        }

        if (age >= i) {
            alpha = Mth.clamp(alpha - 0.0125F, 0, 1);
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<DirectionParticleOptions> {

        @Override
        public Particle createParticle(DirectionParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new EggSplatParticle(level, x, y, z, options.direction(), sprites.get(random));
        }
    }
}

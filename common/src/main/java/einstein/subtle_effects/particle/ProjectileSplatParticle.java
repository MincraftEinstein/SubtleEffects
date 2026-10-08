package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.ProjectileSplatParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import static einstein.subtle_effects.util.Util.radians;

public class ProjectileSplatParticle extends FlatPlaneParticle {

    private final Direction direction;
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    private final BlockPos blockPos;

    protected ProjectileSplatParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, ProjectileSplatParticleOptions options) {
        super(level, x, y, z, sprite);
        direction = options.direction();
        blockPos = options.pos();
        rotation = direction.getRotation().rotateX(radians(-90));
        lifetime = 120;
        quadSize = 0.2F;
        scale(1.5F);

        if (direction.getAxis().isVertical()) {
            rotation.rotateZ(radians(90 * random.nextInt(3)));
        }
    }

    @Override
    public void tick() {
        super.tick();

        pos.set(x, y, z);
        BlockState state = level.getBlockState(blockPos);
        if (state.isAir() || (blockPos.equals(pos) && state.isCollisionShapeFullBlock(level, pos))) {
            remove();
            return;
        }

        int i = (lifetime / 3) * 2;
        if (age == i) {
            if (direction.getAxis().isHorizontal() && level.getFluidState(pos).isEmpty()) {
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

    public record Provider(SpriteSet sprites) implements ParticleProvider<ProjectileSplatParticleOptions> {

        @Override
        public Particle createParticle(ProjectileSplatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new ProjectileSplatParticle(level, x, y, z, sprites.get(random), options);
        }
    }
}

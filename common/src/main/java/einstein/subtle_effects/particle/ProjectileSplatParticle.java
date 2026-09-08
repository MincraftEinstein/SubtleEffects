package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.ProjectileSplatParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ProjectileSplatParticle extends FlatPlaneParticle {

    private final Direction direction;
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    private final BlockPos blockPos;

    protected ProjectileSplatParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, ProjectileSplatParticleOptions options) {
        super(level, x, y, z);
        direction = options.direction();
        blockPos = options.pos();
        rotation = direction.getRotation().rotateX(180 * Mth.DEG_TO_RAD);
        pickSprite(sprites);
        lifetime = 120;
        quadSize = 0.2F;
        scale(1.5F);

        if (direction.getAxis().isVertical()) {
            rotation.rotateY(90 * random.nextInt(3) * Mth.DEG_TO_RAD);
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
    protected void renderVertex(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = new Vector3f(xOffset, 0, yOffset).rotate(rotation).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ProjectileSplatParticleOptions> {

        @Override
        public Particle createParticle(ProjectileSplatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ProjectileSplatParticle(level, x, y, z, sprites, options);
        }
    }
}

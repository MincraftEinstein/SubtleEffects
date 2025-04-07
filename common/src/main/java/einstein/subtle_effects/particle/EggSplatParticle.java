package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.DirectionParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class EggSplatParticle extends FlatPlaneParticle {

    private final Direction direction;

    protected EggSplatParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, Direction direction) {
        super(level, x, y, z);
        this.direction = direction;
        rotation = direction.getRotation().rotateX(180 * Mth.DEG_TO_RAD);
        pickSprite(sprites);
        lifetime = 120;
        quadSize = 0.2F;

        if (direction.getAxis().isVertical()) {
            rotation.rotateY(90 * random.nextInt(3) * Mth.DEG_TO_RAD);
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
    protected void renderVertex(VertexConsumer buffer, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = new Vector3f(xOffset, 0, yOffset).rotate(rotation).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(rCol, gCol, bCol, alpha).setLight(packedLight);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<DirectionParticleOptions> {

        @Override
        public Particle createParticle(DirectionParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new EggSplatParticle(level, x, y, z, sprites, options.direction());
        }
    }
}

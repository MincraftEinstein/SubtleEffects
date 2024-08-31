package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SlimeTrailParticle extends TextureSheetParticle {

    private final int rotation;
    private final BlockPos pos;

    protected SlimeTrailParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, float scale) {
        super(level, x, y, z);
        pickSprite(sprites);
        quadSize = 0.5F * scale;
        setSize(quadSize + 1, 0.1F);
        lifetime = (int) Math.min(300 + (200 * scale), 1200);
        rotation = random.nextInt(3);
        pos = new BlockPos.MutableBlockPos(x, y, z);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        Vec3 pos = camera.getPosition();
        double x = Mth.lerp(partialTick, xo, this.x) - pos.x();
        double y = Mth.lerp(partialTick, yo, this.y) - pos.y();
        double z = Mth.lerp(partialTick, zo, this.z) - pos.z();
        Vec3[] vec3s = {new Vec3(-1, -1, 0), new Vec3(-1, 1, 0), new Vec3(1, 1, 0), new Vec3(1, -1, 0)};

        for (int i = 0; i < vec3s.length; i++) {
            vec3s[i] = vec3s[i].xRot(toRot(-90))
                    .yRot(toRot(90 * rotation))
                    .scale(getQuadSize(partialTick)).add(x, y, z);
        }

        float minU = getU0();
        float maxU = getU1();
        float minV = getV0();
        float maxV = getV1();
        int lightColor = getLightColor(partialTick);

        consumer.addVertex((float) vec3s[0].x(), (float) vec3s[0].y(), (float) vec3s[0].z())
                .setUv(maxU, maxV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);

        consumer.addVertex((float) vec3s[1].x(), (float) vec3s[1].y(), (float) vec3s[1].z())
                .setUv(maxU, minV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);

        consumer.addVertex((float) vec3s[2].x(), (float) vec3s[2].y(), (float) vec3s[2].z())
                .setUv(minU, minV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);

        consumer.addVertex((float) vec3s[3].x(), (float) vec3s[3].y(), (float) vec3s[3].z())
                .setUv(minU, maxV)
                .setColor(rCol, gCol, bCol, alpha)
                .setLight(lightColor);
    }

    private static float toRot(float degrees) {
        return degrees * 0.017453292F;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<FloatParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(FloatParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SlimeTrailParticle(level, sprites, x, y, z, Math.min(options.f(), 64));
        }
    }
}

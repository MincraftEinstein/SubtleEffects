package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.ColorParticleOptions;
import einstein.subtle_effects.util.SparkType;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class SparkParticle extends TextureSheetParticle {

    public static final List<Integer> DEFAULT_COLORS = List.of(0xFFD800, 0xFF6A00);
    public static final List<Integer> SOUL_COLORS = List.of(0x60F5FA, 0x01A7AC);
    public static final List<Integer> BLAZE_COLORS = List.of(0xFFF847, 0xFFD528, 0xD17800, 0x8B3401);

    protected SparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float lifeTimeModifier, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        friction = 0.96F;
        gravity = -0.1F;
        xd = (xd * 0.1) + xSpeed;
        yd = (yd * 0.1) + ySpeed;
        zd = (zd * 0.1) + zSpeed;
        int i = random.nextInt(11);
        quadSize *= (0.75F * i / 10) * ModConfigs.GENERAL.sparksScale.get();
        lifetime = (int) (20 / (random.nextFloat() * 0.8 + 0.2) * i / lifeTimeModifier);
        lifetime = Math.max(lifetime, 1);
        hasPhysics = true;
        speedUpWhenYMotionIsBlocked = true;
        pickSprite(sprites);
    }

    public static ColorParticleOptions create(SparkType sparkType, RandomSource random) {
        return create(sparkType, random, DEFAULT_COLORS);
    }

    public static ColorParticleOptions create(SparkType sparkType, RandomSource random, List<Integer> colors) {
        return new ColorParticleOptions(sparkType.getType().get(), Vec3.fromRGB24(colors.get(random.nextInt(colors.size()))).toVector3f());
    }

    public static ColorParticleOptions createSoul(SparkType sparkType, RandomSource random) {
        return create(sparkType, random, SOUL_COLORS);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return quadSize * Mth.clamp((age + partialTicks) / lifetime * 32, 0, 1);
    }

    @Override
    public int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    public record LongLifeProvider(SpriteSet sprites) implements ParticleProvider<ColorParticleOptions> {

        @Override
        public Particle createParticle(ColorParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 10, sprites);
            Vector3f color = options.getColor();
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }

    public record ShortLifeProvider(SpriteSet sprites) implements ParticleProvider<ColorParticleOptions> {

        @Override
        public Particle createParticle(ColorParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
            Vector3f color = options.getColor();
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }

    public record FloatingProvider(SpriteSet sprites) implements ParticleProvider<ColorParticleOptions> {

        @Override
        public Particle createParticle(ColorParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
            Vector3f color = options.getColor();

            particle.gravity = 0;
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }

    public record MetalProvider(SpriteSet sprites) implements ParticleProvider<ColorParticleOptions> {

        @Override
        public Particle createParticle(ColorParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 20, sprites);
            Vector3f color = options.getColor();

            particle.gravity = 1;
            particle.setColor(color.x(), color.y(), color.z());
            return particle;
        }
    }
}

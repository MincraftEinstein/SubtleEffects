package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.PotionRingParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PotionRingParticle extends FlatPlaneParticle {

    @Nullable
    private final Entity entity;
    private final boolean hasEntity;
    private final boolean isHarmful;
    private double yDistance;
    private final LifetimeAlpha fadeInLifetimeAlpha = new LifetimeAlpha(0, ModConfigs.ENTITIES.humanoids.potionRingsAlpha.get(), 0, 0.5F);
    private final LifetimeAlpha fadeOutLifetimeAlpha = new LifetimeAlpha(ModConfigs.ENTITIES.humanoids.potionRingsAlpha.get(), 0, 0.5F, 1);

    protected PotionRingParticle(ClientLevel level, double x, double y, double z, PotionRingParticleOptions options, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.entity = level.getEntity(options.entityId());
        hasEntity = entity != null;
        isHarmful = options.isHarmful();
        yDistance = hasEntity ? y - entity.getY() : 0;
        lifetime = 10;
        alpha = 0;
        quadSize = 0.2F;
        rotation.rotateX(-90 * Mth.DEG_TO_RAD);
        float scale = ModConfigs.ENTITIES.humanoids.potionRingsScale.get();
        quadSize = 0.5F * scale;
        setSize(1 * scale, 0.1F);

        Vector3f color = options.provider().provideColor(level, x, y, z, random);
        setColor(color.x(), color.y(), color.z());
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTicks) {
        alpha = Math.min(fadeInLifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks), fadeOutLifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks));
        super.extract(state, camera, partialTicks);
    }

    @Override
    public void tick() {
        if (age++ >= lifetime) {
            remove();
            return;
        }

        float halfLife = lifetime / 2F;
        yd += (0.25 / halfLife) * (age > halfLife ? -1 : 1) * (isHarmful ? -1 : 1);

        xo = x;
        yo = y;
        zo = z;

        move(0, yd, 0);
        yd *= friction;

        if (hasEntity) {
            yDistance += y - yo;
            // noinspection all
            setPos(entity.getX(), entity.getY() + yDistance, entity.getZ());
        }
    }

    @Override
    public void move(double x, double y, double z) {
        if (y != 0) {
            setBoundingBox(getBoundingBox().move(x, y, z));
            setLocationFromBoundingbox();
        }
    }

    @Override
    protected int getLightCoords(float partialTick) {
        return ModConfigs.ENTITIES.humanoids.glowingPotionRings.get() ? Util.PARTICLE_LIGHT_COLOR : super.getLightCoords(partialTick);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<PotionRingParticleOptions> {

        @Override
        public Particle createParticle(PotionRingParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new PotionRingParticle(level, x, y, z, options, sprites.get(random));
        }
    }
}

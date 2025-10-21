package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import einstein.subtle_effects.util.LifetimeAlpha;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class PotionRingParticle extends FlatPlaneParticle {

    @Nullable
    private final Entity entity;
    private final boolean hasEntity;
    private double yDistance;
    private final LifetimeAlpha fadeInLifetimeAlpha = new LifetimeAlpha(0, ModConfigs.ENTITIES.humanoids.potionRingsAlpha.get(), 0, 0.5F);
    private final LifetimeAlpha fadeOutLifetimeAlpha = new LifetimeAlpha(ModConfigs.ENTITIES.humanoids.potionRingsAlpha.get(), 0, 0.5F, 1);

    protected PotionRingParticle(ClientLevel level, double x, double y, double z, @Nullable Entity entity, SpriteSet sprites) {
        super(level, x, y, z);
        this.entity = entity;
        hasEntity = entity != null;
        yDistance = hasEntity ? y - entity.getY() : 0;
        lifetime = 10;
        alpha = 0;
        quadSize = 0.2F;
        rotation.rotateX(-90 * Mth.DEG_TO_RAD);
        pickSprite(sprites);
        scale(3 * ModConfigs.ENTITIES.humanoids.potionRingsScale.get());
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        alpha = Math.min(fadeInLifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks), fadeOutLifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks));
        super.render(consumer, camera, partialTicks);
    }

    @Override
    public void tick() {
        if (age++ >= lifetime) {
            remove();
            return;
        }

        float halfLife = lifetime / 2F;
        yd += (0.25 / halfLife) * (age > halfLife ? -1 : 1);

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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorAndIntegerParticleOptions> {

        @Override
        public Particle createParticle(ColorAndIntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PotionRingParticle particle = new PotionRingParticle(level, x, y, z, level.getEntity(options.integer()), sprites);
            Util.setColorFromHex(particle, options.color());
            return particle;
        }
    }
}

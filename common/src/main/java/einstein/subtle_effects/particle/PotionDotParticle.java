package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.PotionRingParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PotionDotParticle extends TextureSheetParticle {

    @Nullable
    private final Entity entity;
    private final boolean hasEntity;
    private double xDistance = 0;
    private double yDistance = 0;
    private double zDistance = 0;

    protected PotionDotParticle(ClientLevel level, double x, double y, double z, PotionRingParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z);
        this.entity = level.getEntity(options.entityId());
        yd = Mth.nextDouble(random, 0.15, 0.3) * (options.isHarmful() ? -1 : 1);
        lifetime = Mth.nextInt(random, 8, 12);
        pickSprite(sprites);
        quadSize *= 1.3F;
        hasEntity = entity != null;
        alpha = ModConfigs.ENTITIES.humanoids.potionRingsAlpha.get();

        if (hasEntity) {
            xDistance = x - entity.getX();
            yDistance = y - entity.getY();
            zDistance = z - entity.getZ();
        }

        Vector3f color = options.provider().provideColor(level, x, y, z, random);
        float colorIntensity = 0.30F;
        float whiteIntensity = 1 - colorIntensity;
        setColor(
                whiteIntensity + (colorIntensity * color.x()),
                whiteIntensity + (colorIntensity * color.y()),
                whiteIntensity + (colorIntensity * color.z())
        );
    }

    @Override
    public void tick() {
        if (age++ >= lifetime || onGround) {
            remove();
            return;
        }

        xo = x;
        yo = y;
        zo = z;

        move(0, yd, 0);
        yd *= friction;

        if (hasEntity) {
            yDistance += y - yo;
            // noinspection all
            setPos(entity.getX() + xDistance, entity.getY() + yDistance, entity.getZ() + zDistance);
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
    protected int getLightColor(float partialTick) {
        return ModConfigs.ENTITIES.humanoids.glowingPotionRings.get() ? Util.PARTICLE_LIGHT_COLOR : super.getLightColor(partialTick);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record PotionDotProvider(SpriteSet sprites) implements ParticleProvider<PotionRingParticleOptions> {

        @Override
        public Particle createParticle(PotionRingParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionDotParticle(level, x, y, z, options, sprites);
        }
    }
}

package einstein.subtle_effects.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class PotionDotParticle extends TextureSheetParticle {

    @Nullable
    private final Entity entity;
    private final boolean hasEntity;
    private double xDistance = 0;
    private double yDistance = 0;
    private double zDistance = 0;

    protected PotionDotParticle(ClientLevel level, double x, double y, double z, int color, @Nullable Entity entity, SpriteSet sprites) {
        super(level, x, y, z);
        this.entity = entity;
        yd = 0.20;
        lifetime = Mth.nextInt(random, 8, 12);
        pickSprite(sprites);
        scale(1.3F);
        hasEntity = entity != null;
        alpha = ModConfigs.ENTITIES.humanoids.potionRingsAlpha.get();

        if (hasEntity) {
            xDistance = x - entity.getX();
            yDistance = y - entity.getY();
            zDistance = z - entity.getZ();
        }

        float colorIntensity = 0.30F;
        float whiteIntensity = 1 - colorIntensity;
        setColor(
                whiteIntensity + (colorIntensity * (ARGB.red(color) / 255F)),
                whiteIntensity + (colorIntensity * (ARGB.green(color) / 255F)),
                whiteIntensity + (colorIntensity * (ARGB.blue(color) / 255F))
        );
    }

    @Override
    public void tick() {
        if (age++ >= lifetime) {
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void move(double x, double y, double z) {
        if (y != 0) {
            setBoundingBox(getBoundingBox().move(x, y, z));
            setLocationFromBoundingbox();
        }
    }

    public record PotionDotProvider(SpriteSet sprites) implements ParticleProvider<ColorAndIntegerParticleOptions> {

        @Override
        public Particle createParticle(ColorAndIntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionDotParticle(level, x, y, z, options.color(), level.getEntity(options.integer()), sprites);
        }
    }
}

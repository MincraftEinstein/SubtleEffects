package einstein.subtle_effects.particle.emitter;

import einstein.subtle_effects.configs.entities.HumanoidConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class PotionEmitter extends NoRenderParticle {

    private final int color;
    private final int entityId;
    @Nullable
    private final Entity entity;

    protected PotionEmitter(ClientLevel level, double x, double y, double z, int color, int entityId) {
        super(level, x, y, z);
        this.color = color;
        this.entityId = entityId;
        Util.setColorFromHex(this, color);
        entity = level.getEntity(entityId);
        lifetime = 1;
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

        if (entity != null) {
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
        }

        if (ModConfigs.ENTITIES.humanoids.potionRingsParticleType == HumanoidConfigs.PotionRingsParticleType.BOTH || ModConfigs.ENTITIES.humanoids.potionRingsParticleType == HumanoidConfigs.PotionRingsParticleType.RINGS_ONLY) {
            for (int i = 0; i < 3; i++) {
                level.addParticle(new ColorAndIntegerParticleOptions(ModParticles.POTION_RING.get(), color, entityId),
                        x, y - 0.1 + (0.4 * i), z,
                        0, 0, 0
                );
            }
        }

        if (ModConfigs.ENTITIES.humanoids.potionRingsParticleType == HumanoidConfigs.PotionRingsParticleType.BOTH || ModConfigs.ENTITIES.humanoids.potionRingsParticleType == HumanoidConfigs.PotionRingsParticleType.DOTS_ONLY) {
            for (int i = 0; i < 20; i++) {
                level.addParticle(new ColorAndIntegerParticleOptions(ModParticles.POTION_DOT.get(), color, entityId),
                        x + MathUtil.nextNonAbsDouble(random, 0.75),
                        y + random.nextDouble() - 0.5,
                        z + MathUtil.nextNonAbsDouble(random, 0.75),
                        0, 0, 0
                );
            }
        }
    }

    public record Provider() implements ParticleProvider<ColorAndIntegerParticleOptions> {

        @Override
        public Particle createParticle(ColorAndIntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new PotionEmitter(level, x, y, z, options.color(), options.integer());
        }
    }
}

package einstein.subtle_effects.particle.emitter;

import einstein.subtle_effects.configs.entities.HumanoidConfigs;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.PotionRingParticleOptions;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class PotionEmitter extends NoRenderParticle {

    private final ColorProviderType.ColorProvider colorProvider;
    private final boolean isHarmful;
    private final int entityId;
    @Nullable
    private final Entity entity;

    protected PotionEmitter(ClientLevel level, double x, double y, double z, ColorProviderType.ColorProvider colorProvider, boolean isHarmful, int entityId) {
        super(level, x, y, z);
        this.colorProvider = colorProvider;
        this.isHarmful = isHarmful && ModConfigs.ENTITIES.humanoids.reverseDirectionForHarmfulEffects.get();
        this.entityId = entityId;
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
            y = entity.getY() + (isHarmful ? entity.getBbHeight() : 0);
            z = entity.getZ();
        }

        HumanoidConfigs.PotionRingsParticleType type = ModConfigs.ENTITIES.humanoids.potionRingsParticleType.get();
        if (type == HumanoidConfigs.PotionRingsParticleType.BOTH || type == HumanoidConfigs.PotionRingsParticleType.RINGS_ONLY) {
            for (int i = 0; i < 3; i++) {
                level.addParticle(new PotionRingParticleOptions(ModParticles.POTION_RING.get(), colorProvider, isHarmful, entityId),
                        x, y - (isHarmful ? 0.5 : 0) - 0.1 + (0.4 * i), z,
                        0, 0, 0
                );
            }
        }

        if (type == HumanoidConfigs.PotionRingsParticleType.BOTH || type == HumanoidConfigs.PotionRingsParticleType.DOTS_ONLY) {
            float scale = ModConfigs.ENTITIES.humanoids.potionRingsScale.get();
            for (int i = 0; i < 20; i++) {
                level.addParticle(new PotionRingParticleOptions(ModParticles.POTION_DOT.get(), colorProvider, isHarmful, entityId),
                        x + MathUtil.nextNonAbsDouble(random, 0.75 * scale),
                        y + random.nextDouble() + (isHarmful ? 0.5 : -0.5),
                        z + MathUtil.nextNonAbsDouble(random, 0.75 * scale),
                        0, 0, 0
                );
            }
        }
    }

    public record Provider() implements ParticleProvider<PotionRingParticleOptions> {

        @Override
        public Particle createParticle(PotionRingParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionEmitter(level, x, y, z, options.provider(), options.isHarmful(), options.entityId());
        }
    }
}

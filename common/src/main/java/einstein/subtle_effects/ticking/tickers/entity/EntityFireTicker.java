package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.DyedFlamesCompat;
import einstein.subtle_effects.compat.PrometheusCompat;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.SparkType;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class EntityFireTicker extends EntityTicker<Entity> {

    private final float bbWidth;
    private final float bbHeight;
    private final boolean isBlaze = entity.getType().equals(EntityType.BLAZE);

    public EntityFireTicker(Entity entity) {
        super(entity);
        bbWidth = entity.getBbWidth();
        bbHeight = entity.getBbHeight();
    }

    @Override
    public void entityTick() {
        if (entity.displayFireAnimation() || isBlaze) {
            float volume = ENTITIES.burning.soundVolume.get();
            if (volume > 0 && random.nextInt(90) == 0) {
                Util.playClientSound(entity, SoundEvents.FIRE_EXTINGUISH, entity.getSoundSource(), volume, 1);
            }

            if (bbWidth <= 4 && bbHeight <= 4) {
                if (!isBlaze && ENTITIES.burning.smokeType.isEnabled() && random.nextDouble() < ENTITIES.burning.smokeDensity.get()) {
                    level.addParticle(ENTITIES.burning.smokeType.getParticle().get(),
                            entity.getRandomX(1),
                            entity.getRandomY(),
                            entity.getRandomZ(1),
                            0, 0, 0
                    );
                }

                if (random.nextDouble() < ENTITIES.burning.sparksDensity.get()) {
                    List<Integer> colors = getSparkColors();
                    if (colors != null) {
                        for (int i = 0; i < 2; i++) {
                            level.addParticle(SparkParticle.create(SparkType.SHORT_LIFE, random, colors),
                                    entity.getRandomX(1),
                                    entity.getRandomY(),
                                    entity.getRandomZ(1),
                                    nextNonAbsDouble(random, 0.03),
                                    nextNonAbsDouble(random, 0.05),
                                    nextNonAbsDouble(random, 0.03)
                            );
                        }
                    }
                }

                if (bbWidth < 2 && bbHeight < 2 && !random.nextBoolean()) {
                    return;
                }

                if (random.nextDouble() < ENTITIES.burning.flamesDensity.get()) {
                    ParticleOptions options = getFlameParticle();
                    if (options != null) {
                        level.addParticle(options,
                                entity.getRandomX(1),
                                entity.getRandomY(),
                                entity.getRandomZ(1),
                                0, 0, 0
                        );
                    }
                }
            }
        }
    }

    @Nullable
    private ParticleOptions getFlameParticle() {
        if (CompatHelper.IS_PROMETHEUS_LOADED.get()) {
            return PrometheusCompat.getFlameParticle(entity);
        }
        else if (CompatHelper.IS_DYED_FLAMES_LOADED.get()) {
            return DyedFlamesCompat.getFlameParticle(entity);
        }
        return ParticleTypes.FLAME;
    }

    @Nullable
    private List<Integer> getSparkColors() {
        if (isBlaze) {
            return SparkParticle.BLAZE_COLORS;
        }
        else if (CompatHelper.IS_PROMETHEUS_LOADED.get()) {
            return PrometheusCompat.getSparkParticle(entity);
        }
        else if (CompatHelper.IS_DYED_FLAMES_LOADED.get()) {
            return DyedFlamesCompat.getSparkParticle(entity);
        }
        return SparkParticle.DEFAULT_COLORS;
    }
}

package einstein.subtle_effects.tickers;

import einstein.subtle_effects.configs.SmokeType;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class EntityFireTicker extends Ticker<Entity> {

    private final float bbWidth;
    private final float bbHeight;

    public EntityFireTicker(Entity entity) {
        super(entity);
        bbWidth = entity.getBbWidth();
        bbHeight = entity.getBbHeight();
    }

    @Override
    public void tick() {
        if (entity.isOnFire()) {
            if (entity.isSpectator()) {
                return;
            }

            if (random.nextInt(90) == 0 && ENTITIES.burning.sounds) {
                Util.playClientSound(entity, SoundEvents.FIRE_EXTINGUISH, entity.getSoundSource(), 0.3F, 1);
            }

            if (bbWidth <= 4 && bbHeight <= 4) {
                if (ENTITIES.burning.smoke != SmokeType.OFF) {
                    level.addParticle(ENTITIES.burning.smoke.getParticle().get(),
                            entity.getRandomX(1),
                            entity.getRandomY(),
                            entity.getRandomZ(1),
                            0, 0, 0
                    );
                }

                if (ENTITIES.burning.sparks) {
                    for (int i = 0; i < 2; i++) {
                        level.addParticle(ModParticles.SHORT_SPARK.get(),
                                entity.getRandomX(1),
                                entity.getRandomY(),
                                entity.getRandomZ(1),
                                nextNonAbsDouble(random, 0.03),
                                nextNonAbsDouble(random, 0.05),
                                nextNonAbsDouble(random, 0.03)
                        );
                    }
                }

                if (bbWidth < 2 && bbHeight < 2 && !random.nextBoolean()) {
                    return;
                }

                if (ENTITIES.burning.flames) {
                    level.addParticle(ParticleTypes.FLAME,
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

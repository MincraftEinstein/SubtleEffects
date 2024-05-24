package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

import static einstein.subtle_effects.init.ModConfigs.INSTANCE;
import static einstein.subtle_effects.util.MathUtil.nextFloat;
import static einstein.subtle_effects.util.MathUtil.nextSign;

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

            if (random.nextInt(90) == 0 && INSTANCE.burningEntitySounds.get()) {
                Util.playClientSound(entity.getSoundSource(), entity, SoundEvents.FIRE_EXTINGUISH, 0.3F, 1);
            }

            if (bbWidth <= 4 && bbHeight <= 4) {
                if (INSTANCE.burningEntitySmoke.get() != ModConfigs.SmokeType.OFF) {
                    level.addParticle(INSTANCE.burningEntitySmoke.get().getParticle().get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
                }

                if (INSTANCE.burningEntitySparks.get()) {
                    for (int i = 0; i < 2; i++) {
                        level.addParticle(ModParticles.SHORT_SPARK.get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1),
                                nextFloat(3) * nextSign(),
                                nextFloat(5) * nextSign(),
                                nextFloat(3) * nextSign()
                        );
                    }
                }

                if (bbWidth < 2 && bbHeight < 2 && !random.nextBoolean()) {
                    return;
                }

                if (INSTANCE.burningEntityFlames.get()) {
                    level.addParticle(ParticleTypes.FLAME, entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
                }
            }
        }
    }
}

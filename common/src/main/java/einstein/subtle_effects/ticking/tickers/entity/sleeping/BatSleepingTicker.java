package einstein.subtle_effects.ticking.tickers.entity.sleeping;

import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.ambient.Bat;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class BatSleepingTicker extends SleepingTicker<Bat> {

    public BatSleepingTicker(Bat entity) {
        super(entity);
    }

    @Override
    protected boolean isSleeping() {
        return ENTITIES.sleeping.batsHaveSleepingZs && entity.isResting();
    }

    @Override
    protected ParticleOptions getParticle() {
        return ModParticles.FALLING_SNORING.get();
    }

    @Override
    protected boolean particleConfigEnabled() {
        return ENTITIES.sleeping.batsHaveSleepingZs;
    }
}

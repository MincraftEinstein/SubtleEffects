package einstein.subtle_effects.ticking.tickers.entity.sleeping;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SleepingTicker<T extends LivingEntity> extends EntityTicker<T> {

    private final SoundEvent snoreSound;
    private final float snoreVolume;
    private final int breathDelay;
    private final boolean doesSnore;
    private boolean firstSleepTick = true;
    private boolean isFirstBreath = true;
    private int snoreStartDelay = 0;
    private int delayTimer = 0;
    private int breatheTimer = 0;
    private int snoreTimer = 0;
    private int zCount = 0;

    public SleepingTicker(T entity) {
        this(entity, true, Util.BREATH_DELAY, null, 0);
    }

    public SleepingTicker(T entity, boolean doesSnore, int breathDelay, SoundEvent snoreSound, float snoreVolume) {
        super(entity);
        this.doesSnore = doesSnore;
        this.snoreVolume = snoreVolume;
        this.snoreSound = snoreSound;
        this.breathDelay = breathDelay;
    }

    @Override
    public void entityTick() {
        if (isSleeping()) {
            if (firstSleepTick) {
                snoreStartDelay = (shouldDelay() ? Util.BREATH_DELAY + random.nextInt(40) : 0);
                firstSleepTick = false;
                return;
            }

            if (delayTimer < snoreStartDelay) {
                delayTimer++;
                return;
            }

            if (!isFirstBreath && breatheTimer < breathDelay) {
                breatheTimer++;
                return;
            }

            if (snoreTimer >= Util.SNORE_DELAY) {
                if (doesSnore && !entity.isSilent() && zCount <= 0 && snoreSound != null) {
                    Util.playClientSound(entity, snoreSound, SoundSource.NEUTRAL, snoreVolume, entity.getVoicePitch());
                }

                snoreTimer = 0;
                zCount++;

                if (particleConfigEnabled()) {
                    if (!ENTITIES.sleeping.displaySleepingZsOnlyWhenSnoring || doesSnore) {
                        level.addParticle(getParticle(), entity.getX(), entity.getY() + 0.5, entity.getZ(), 0, 0, 0);
                    }
                }

                if (zCount >= Util.MAX_Z_COUNT) {
                    zCount = 0;
                    breatheTimer = 0;
                    isFirstBreath = false;
                }
            }

            if (snoreTimer < Util.SNORE_DELAY) {
                snoreTimer++;
            }

            return;
        }

        firstSleepTick = true;
        isFirstBreath = true;
        snoreStartDelay = 0;
        delayTimer = 0;
        breatheTimer = 0;
        snoreTimer = 0;
        zCount = 0;
    }

    protected boolean isSleeping() {
        return entity.isSleeping();
    }

    protected ParticleOptions getParticle() {
        return ModParticles.SNORING.get();
    }

    protected boolean shouldDelay() {
        return true;
    }

    protected boolean particleConfigEnabled() {
        return ENTITIES.sleeping.otherMobsHaveSleepingZs;
    }

    protected static boolean doesEntitySnore(LivingEntity entity, double chance) {
        UUID uuid = entity.getUUID();
        return Double.parseDouble("0." + Math.abs(uuid.hashCode())) < chance;
    }
}

package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SleepingTicker extends Ticker<LivingEntity> {

    private SoundEvent snoreSound = null;
    private boolean shouldDelay = true;
    private boolean doesSnore = true;
    private boolean firstSleepTick = true;
    private boolean isFirstBreath = true;
    private boolean isBat = false;
    private int breathDelay = Util.BREATH_DELAY;
    private int snoreStartDelay = 0;
    private int delayTimer = 0;
    private int breatheTimer = 0;
    private int snoreTimer = 0;
    private int zCount = 0;

    public SleepingTicker(LivingEntity entity) {
        super(entity);
        switch (entity) {
            case Player player -> {
                doesSnore = doesEntitySnore(player, ENTITIES.sleeping.playerSnoreChance.get());
                snoreSound = ModSounds.PLAYER_SNORE.get();
                if (player.isLocalPlayer()) {
                    shouldDelay = false;
                    breathDelay = 40;
                }
            }
            case Villager villager -> {
                doesSnore = doesEntitySnore(entity, ENTITIES.sleeping.villagerSnoreChance.get());
                snoreSound = ModSounds.VILLAGER_SNORE.get();
                breathDelay = 80;
            }
            case Bat bat -> isBat = true;
            default -> {
            }
        }
    }

    @Override
    public void tick() {
        if (entity.isSleeping() || (entity instanceof Cat cat && cat.isLying()) || (isBat && ((Bat) entity).isResting())) {
            if (firstSleepTick) {
                snoreStartDelay = (shouldDelay ? Util.BREATH_DELAY + random.nextInt(40) : 0);
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
                    Util.playClientSound(SoundSource.NEUTRAL, entity, snoreSound, 1, entity.getVoicePitch());
                }

                snoreTimer = 0;
                zCount++;
                if (ENTITIES.sleeping.sleepingZs) {
                    if ((entity instanceof Fox && ENTITIES.sleeping.foxesHaveSleepingZs)
                            || (!(entity instanceof Fox)
                            && (!ENTITIES.sleeping.displaySleepingZsOnlyWhenSnoring || doesSnore))) {
                        level.addParticle(isBat ? ModParticles.FALLING_SNORING.get() : ModParticles.SNORING.get(), entity.getX(), entity.getY() + 0.5, entity.getZ(), 0, 0, 0);
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
        }
        else {
            firstSleepTick = true;
            isFirstBreath = true;
            snoreStartDelay = 0;
            delayTimer = 0;
            breatheTimer = 0;
            snoreTimer = 0;
            zCount = 0;
        }
    }

    private static boolean doesEntitySnore(LivingEntity entity, double chance) {
        UUID uuid = entity.getUUID();
        return Double.parseDouble("0." + Math.abs(uuid.hashCode())) < chance;
    }
}

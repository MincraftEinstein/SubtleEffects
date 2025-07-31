package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class StomachGrowlingTicker extends EntityTicker<Player> {

    private int growlTimer = 0;

    public StomachGrowlingTicker(Player entity) {
        super(entity);
    }

    @Override
    public void entityTick() {
        if (entity.isCreative() || entity.isSpectator()) {
            return;
        }

        int foodLevel = entity.getFoodData().getFoodLevel();
        if (foodLevel <= ENTITIES.humanoids.player.stomachGrowlingThreshold.get()) {
            if (growlTimer == 0) {
                Util.playClientSound(entity, ModSounds.PLAYER_STOMACH_GROWL.get(), SoundSource.PLAYERS, ENTITIES.humanoids.player.stomachGrowlingVolume.get(), (random.nextBoolean() ? 1 : 1.5F));
            }

            growlTimer++;

            if (growlTimer >= ENTITIES.humanoids.player.stomachGrowlingWaitTime.get() * 20) {
                growlTimer = 0;
            }
        }
    }
}

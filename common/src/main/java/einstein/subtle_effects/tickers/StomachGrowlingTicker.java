package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class StomachGrowlingTicker extends Ticker<Player> {

    private int growlTimer = 0;

    public StomachGrowlingTicker(Player entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (entity.isCreative() || entity.isSpectator()) {
            return;
        }

        int foodLevel = entity.getFoodData().getFoodLevel();
        if (foodLevel <= ModConfigs.ENTITIES.stomachGrowlingThreshold.get()) {
            if (growlTimer == 0) {
                Util.playClientSound(entity, ModSounds.PLAYER_STOMACH_GROWL.get(), SoundSource.PLAYERS, 1, (random.nextBoolean() ? 1 : 1.5F));
            }

            growlTimer++;

            if (growlTimer >= Util.STOMACH_GROWL_DELAY) {
                growlTimer = 0;
            }
        }
    }
}

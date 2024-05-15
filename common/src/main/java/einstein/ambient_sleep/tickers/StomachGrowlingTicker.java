package einstein.ambient_sleep.tickers;

import einstein.ambient_sleep.init.ModSounds;
import einstein.ambient_sleep.util.Util;
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
        if (foodLevel <= 6) {
            if (growlTimer == 0) {
                Util.playClientSound(SoundSource.PLAYERS, entity, ModSounds.PLAYER_STOMACH_GROWL.get(), 1, (random.nextBoolean() ? 1 : 1.5F));
            }

            growlTimer++;

            if (growlTimer >= Util.STOMACH_GROWL_DELAY) {
                growlTimer = 0;
            }
        }
    }
}

package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;

public class HeartbeatTicker extends Ticker<Player> {

    private int beatTimer = 0;

    public HeartbeatTicker(Player entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (entity.isCreative() || entity.isSpectator()) {
            return;
        }

        float health = entity.getHealth();
        if (health <= 6) {
            beatTimer++;
            if (beatTimer >= (health > 4 ? 60 : 20)) {
                beatTimer = 0;
                Util.playClientSound(entity.getSoundSource(), entity, ModSounds.PLAYER_HEARTBEAT.get(), 1, 1);
            }
        }
    }
}

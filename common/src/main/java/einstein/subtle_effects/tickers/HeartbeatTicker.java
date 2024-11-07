package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

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
        int threshold = ENTITIES.heartBeatingThreshold.get();

        if (health <= threshold) {
            beatTimer++;
            if (beatTimer >= (health > ((float) threshold / 2) ? 60 : 20)) {
                beatTimer = 0;
                Util.playClientSound(entity, ModSounds.PLAYER_HEARTBEAT.get(), entity.getSoundSource(), ENTITIES.heartbeatVolume.get(), 1);
            }
        }
    }
}

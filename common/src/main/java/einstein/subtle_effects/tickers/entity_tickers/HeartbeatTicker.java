package einstein.subtle_effects.tickers.entity_tickers;

import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class HeartbeatTicker extends EntityTicker<Player> {

    private int beatTimer = 0;

    public HeartbeatTicker(Player entity) {
        super(entity);
    }

    @Override
    public void entityTick() {
        if (entity.isCreative() || entity.isSpectator()) {
            return;
        }

        float health = entity.getHealth();
        int threshold = ENTITIES.humanoids.player.heartBeatingThreshold.get();

        if (health <= threshold) {
            beatTimer++;

            int waitTime = ENTITIES.humanoids.player.heartBeatingWaitTime.get() * 20;
            if (beatTimer >= (health > ((float) threshold / 2) ? waitTime : (waitTime / 2))) {
                beatTimer = 0;
                Util.playClientSound(entity, ModSounds.PLAYER_HEARTBEAT.get(), entity.getSoundSource(), ENTITIES.humanoids.player.heartbeatVolume.get(), 1);
            }
        }
    }
}

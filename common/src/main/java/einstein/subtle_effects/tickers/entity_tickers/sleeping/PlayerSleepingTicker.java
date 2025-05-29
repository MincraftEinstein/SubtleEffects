package einstein.subtle_effects.tickers.entity_tickers.sleeping;

import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class PlayerSleepingTicker extends SleepingTicker<Player> {

    public PlayerSleepingTicker(Player player) {
        super(player,
                doesEntitySnore(player, ENTITIES.sleeping.playerSnoreChance.get()),
                player.isLocalPlayer() ? 40 : Util.BREATH_DELAY,
                ModSounds.PLAYER_SNORE.get(),
                ENTITIES.sleeping.playerSnoreSoundVolume.get()
        );
    }

    @Override
    protected boolean shouldDelay() {
        return !entity.isLocalPlayer();
    }

    @Override
    protected boolean particleConfigEnabled() {
        return ENTITIES.sleeping.playersHaveSleepingZs;
    }
}

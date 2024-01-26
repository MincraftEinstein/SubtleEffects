package einstein.ambient_sleep.init;

import einstein.ambient_sleep.platform.Services;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSounds {

    public static final Supplier<SoundEvent> VILLAGER_SNORE = Services.REGISTRY.registerSound("entity.villager.snore");
    public static final Supplier<SoundEvent> PLAYER_SNORE = Services.REGISTRY.registerSound("entity.player.snore");

    public static void init() {
    }
}

package einstein.subtle_effects.init;

import einstein.subtle_effects.platform.Services;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSounds {

    public static final Supplier<SoundEvent> VILLAGER_SNORE = Services.REGISTRY.registerSound("entity.villager.snore");
    public static final Supplier<SoundEvent> PLAYER_SNORE = Services.REGISTRY.registerSound("entity.player.snore");
    public static final Supplier<SoundEvent> PLAYER_STOMACH_GROWL = Services.REGISTRY.registerSound("entity.player.stomach_growl");
    public static final Supplier<SoundEvent> PLAYER_HEARTBEAT = Services.REGISTRY.registerSound("entity.player.heartbeat");
    public static final Supplier<SoundEvent> AMETHYST_CLUSTER_CHIME = Services.REGISTRY.registerSound("block.amethyst_cluster.chime");

    public static void init() {
    }
}

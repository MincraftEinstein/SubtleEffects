package einstein.subtle_effects.init;

import einstein.subtle_effects.platform.Services;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSounds {

    public static final Supplier<SoundEvent> VILLAGER_SNORE = register("entity.villager.snore");
    public static final Supplier<SoundEvent> PLAYER_SNORE = register("entity.player.snore");
    public static final Supplier<SoundEvent> PLAYER_STOMACH_GROWL = register("entity.player.stomach_growl");
    public static final Supplier<SoundEvent> PLAYER_HEARTBEAT = register("entity.player.heartbeat");
    public static final Supplier<SoundEvent> AMETHYST_CLUSTER_CHIME = register("block.amethyst_cluster.chime");
    public static final Supplier<SoundEvent> CAMPFIRE_SIZZLE = register("block.campfire.sizzle");
    public static final Supplier<SoundEvent> EGG_BREAK = register("entity.egg.break");
    public static final Supplier<SoundEvent> DRIP_WATER = register("block.water.drip");
    public static final Supplier<SoundEvent> DRIP_LAVA = register("block.lava.drip");
    public static final Supplier<SoundEvent> DRIP_WATER_INTO_FLUID = register("block.water.drip_into_fluid");
    public static final Supplier<SoundEvent> DRIP_LAVA_INTO_FLUID = register("block.lava.drip_into_fluid");

    public static void init() {
    }

    private static Supplier<SoundEvent> register(String name) {
        return Services.REGISTRY.registerSound(name);
    }
}

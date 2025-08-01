package einstein.subtle_effects.init;

import einstein.subtle_effects.platform.Services;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSounds {

    public static final Supplier<SoundEvent> VILLAGER_SNORE = register("entity.villager.snore");
    public static final Supplier<SoundEvent> PLAYER_SNORE = register("entity.player.snore");
    public static final Supplier<SoundEvent> PLAYER_STOMACH_GROWL = register("entity.player.stomach_growl");
    public static final Supplier<SoundEvent> PLAYER_HEARTBEAT = register("entity.player.heartbeat");
    public static final Supplier<SoundEvent> EGG_BREAK = register("entity.egg.break");
    public static final Supplier<SoundEvent> SNOWBALL_POOF = register("entity.snowball.poof");
    public static final Supplier<SoundEvent> COW_EAT = register("entity.cow.eat");
    public static final Supplier<SoundEvent> CHICKEN_EAT = register("entity.chicken.eat");
    public static final Supplier<SoundEvent> OCELOT_EAT = register("entity.ocelot.eat");
    public static final Supplier<SoundEvent> SHEEP_EAT = register("entity.sheep.eat");
    public static final Supplier<SoundEvent> AXOLOTL_EAT = register("entity.axolotl.eat");
    public static final Supplier<SoundEvent> BEE_EAT = register("entity.bee.eat");
    public static final Supplier<SoundEvent> TADPOLE_EAT = register("entity.tadpole.eat");
    public static final Supplier<SoundEvent> RABBIT_EAT = register("entity.rabbit.eat");
    public static final Supplier<SoundEvent> TURTLE_EAT = register("entity.turtle.eat");
    public static final Supplier<SoundEvent> HOGLIN_EAT = register("entity.hoglin.eat");
    public static final Supplier<SoundEvent> AMETHYST_CLUSTER_CHIME = register("block.amethyst_cluster.chime");
    public static final Supplier<SoundEvent> CAMPFIRE_SIZZLE = register("block.campfire.sizzle");
    public static final Supplier<SoundEvent> DRIP_WATER = register("block.water.drip");
    public static final Supplier<SoundEvent> DRIP_LAVA = register("block.lava.drip");
    public static final Supplier<SoundEvent> DRIP_WATER_INTO_FLUID = register("block.water.drip_into_fluid");
    public static final Supplier<SoundEvent> DRIP_LAVA_INTO_FLUID = register("block.lava.drip_into_fluid");
    public static final Supplier<SoundEvent> CAULDRON_CLEAN_ITEM = register("block.cauldron.clean_item");
    public static final Supplier<SoundEvent> GEYSER_WHOOSH = register("environment.geyser.whoosh");
    public static final Supplier<SoundEvent> GEYSER_HISS = register("environment.geyser.hiss");
    public static final Supplier<SoundEvent> FIREFLY_BUZZ = register("environment.firefly.buzz");

    public static void init() {
    }

    private static Supplier<SoundEvent> register(String name) {
        return Services.REGISTRY.registerSound(name);
    }
}

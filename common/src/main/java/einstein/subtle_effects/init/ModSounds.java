package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.util.platform.Registrar;
import me.fzzyhmstrs.fzzy_config.util.platform.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    private static final Registrar<SoundEvent> SOUND_EVENTS = ConfigApiJava.platform().createRegistrar(SubtleEffects.MOD_ID, BuiltInRegistries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> VILLAGER_SNORE = register("entity.villager.snore");
    public static final RegistrySupplier<SoundEvent> PLAYER_SNORE = register("entity.player.snore");
    public static final RegistrySupplier<SoundEvent> PLAYER_STOMACH_GROWL = register("entity.player.stomach_growl");
    public static final RegistrySupplier<SoundEvent> PLAYER_HEARTBEAT = register("entity.player.heartbeat");
    public static final RegistrySupplier<SoundEvent> EGG_BREAK = register("entity.egg.break");
    public static final RegistrySupplier<SoundEvent> SNOWBALL_POOF = register("entity.snowball.poof");
    public static final RegistrySupplier<SoundEvent> COW_EAT = register("entity.cow.eat");
    public static final RegistrySupplier<SoundEvent> CHICKEN_EAT = register("entity.chicken.eat");
    public static final RegistrySupplier<SoundEvent> OCELOT_EAT = register("entity.ocelot.eat");
    public static final RegistrySupplier<SoundEvent> SHEEP_EAT = register("entity.sheep.eat");
    public static final RegistrySupplier<SoundEvent> AXOLOTL_EAT = register("entity.axolotl.eat");
    public static final RegistrySupplier<SoundEvent> BEE_EAT = register("entity.bee.eat");
    public static final RegistrySupplier<SoundEvent> TADPOLE_EAT = register("entity.tadpole.eat");
    public static final RegistrySupplier<SoundEvent> RABBIT_EAT = register("entity.rabbit.eat");
    public static final RegistrySupplier<SoundEvent> TURTLE_EAT = register("entity.turtle.eat");
    public static final RegistrySupplier<SoundEvent> HOGLIN_EAT = register("entity.hoglin.eat");
    public static final RegistrySupplier<SoundEvent> ALLAY_TWINKLE = register("entity.allay.twinkle");
    public static final RegistrySupplier<SoundEvent> ALLAY_DUPLICATE = register("entity.allay.duplicate");
    public static final RegistrySupplier<SoundEvent> AMETHYST_CLUSTER_CHIME = register("block.amethyst_cluster.chime");
    public static final RegistrySupplier<SoundEvent> CAMPFIRE_SIZZLE = register("block.campfire.sizzle");
    public static final RegistrySupplier<SoundEvent> DRIP_WATER = register("block.water.drip");
    public static final RegistrySupplier<SoundEvent> DRIP_LAVA = register("block.lava.drip");
    public static final RegistrySupplier<SoundEvent> DRIP_WATER_INTO_FLUID = register("block.water.drip_into_fluid");
    public static final RegistrySupplier<SoundEvent> DRIP_LAVA_INTO_FLUID = register("block.lava.drip_into_fluid");
    public static final RegistrySupplier<SoundEvent> CAULDRON_CLEAN_ITEM = register("block.cauldron.clean_item");
    public static final RegistrySupplier<SoundEvent> MONSTER_SPAWNER_AMBIENT = register("block.monster_spawner.ambient");
    public static final RegistrySupplier<SoundEvent> MONSTER_SPAWNER_SPAWN_MOB = register("block.monster_spawner.spawn_mob");
    public static final RegistrySupplier<SoundEvent> GEYSER_WHOOSH = register("environment.geyser.whoosh");
    public static final RegistrySupplier<SoundEvent> GEYSER_HISS = register("environment.geyser.hiss");
    public static final RegistrySupplier<SoundEvent> FIREFLY_BUZZ = register("environment.firefly.buzz");
    public static final RegistrySupplier<SoundEvent> SPAWN_EGG_SPAWN_MOB = register("item.spawn_egg.spawn_mob");

    public static void init() {
        SOUND_EVENTS.init();
    }

    private static RegistrySupplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(SubtleEffects.loc(name)));
    }
}

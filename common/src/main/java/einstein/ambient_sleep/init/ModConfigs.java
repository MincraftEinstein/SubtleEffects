package einstein.ambient_sleep.init;

import einstein.ambient_sleep.AmbientSleep;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {

    private static final Pair<ModConfigs, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    public static final ModConfigs INSTANCE = SPEC_PAIR.getLeft();
    public static final ForgeConfigSpec SPEC = SPEC_PAIR.getRight();
    private static final String TO_DISABLE = "0 to disable";

    // Flaming Blocks
    public final ForgeConfigSpec.BooleanValue disableDefaultCampfireSparks;
    public final ForgeConfigSpec.BooleanValue enableCandleSparks;
    public final ForgeConfigSpec.BooleanValue enableFurnaceSparks;
    public final ForgeConfigSpec.BooleanValue enableFireSparks;
    public final ForgeConfigSpec.BooleanValue enableCampfireSparks;
    public final ForgeConfigSpec.BooleanValue enableTorchSparks;

    // Blocks
    public final ForgeConfigSpec.BooleanValue redstoneBlockDust;
    public final ForgeConfigSpec.BooleanValue glowstoneBlockDust;
    public final ForgeConfigSpec.BooleanValue beehivesHaveSleepingZs;

    // Entity Snoring
    public final ForgeConfigSpec.DoubleValue playerSnoreChance;
    public final ForgeConfigSpec.DoubleValue villagerSnoreChance;
    public final ForgeConfigSpec.BooleanValue displaySleepingZsOnlyWhenSnoring;
    public final ForgeConfigSpec.BooleanValue foxesHaveSleepingZs;
    public final ForgeConfigSpec.BooleanValue adjustNametagRenderingWhenSleeping;

    // Entity Dust Clouds
    public final ForgeConfigSpec.BooleanValue fallDamageDustClouds;
    public final ForgeConfigSpec.BooleanValue sprintingDustClouds;
    public final ForgeConfigSpec.BooleanValue mobSprintingDustClouds;

    // Entities
    public final ForgeConfigSpec.BooleanValue chickenHitFeathers;
    public final ForgeConfigSpec.BooleanValue parrotHitFeathers;
    public final ForgeConfigSpec.BooleanValue enderPearlTrail;
    public final ForgeConfigSpec.DoubleValue snowballTrailChance;
    public final ForgeConfigSpec.DoubleValue allayMagicChance;

    // General
    public final ForgeConfigSpec.BooleanValue enableSleepingZs;

    public ModConfigs(ForgeConfigSpec.Builder builder) {
        builder.translation(categoryKey("blocks")).push("blocks")
                .translation(categoryKey("flaming_blocks")).push("flamingBlocks");

        disableDefaultCampfireSparks = builder
                .comment("Disables the default lava spark particle from campfires")
                .translation(key("disable_default_campfire_sparks"))
                .define("disableDefaultCampfireSparks", true);

        enableCandleSparks = builder.translation(key("enable_candle_sparks"))
                .define("enableCandleSparks", true);

        enableFurnaceSparks = builder.translation(key("enable_furnace_sparks"))
                .define("enableFurnaceSparks", true);

        enableFireSparks = builder.translation(key("enable_fire_sparks"))
                .define("enableFireSparks", true);

        enableCampfireSparks = builder.translation(key("enable_campfire_sparks"))
                .define("enableCampfireSparks", true);

        enableTorchSparks = builder.translation(key("enable_torch_sparks"))
                .define("enableTorchSparks", true);

        builder.pop();

        redstoneBlockDust = builder
                .translation(key("redstone_block_dust"))
                .define("redstoneBlockDust", true);

        glowstoneBlockDust = builder
                .translation(key("glowstone_block_dust"))
                .define("glowstoneBlockDust", true);

        beehivesHaveSleepingZs = builder
                .comment("Display Z particles in front of bee hives/nests at night")
                .translation(key("beehives_have_sleeping_zs"))
                .define("beehivesHaveSleepingZs", true);

        builder.pop().translation(categoryKey("entities")).push("entities")
                .translation(categoryKey("sleeping")).push("sleeping");

        playerSnoreChance = builder
                .comment("A percentage based chance for a player to snore.", TO_DISABLE)
                .translation(key("player_snore_chance"))
                .defineInRange("playerSnoreChance", 1.0, 0, 1.0);

        villagerSnoreChance = builder
                .comment("A percentage based chance for a villager to snore.", TO_DISABLE)
                .translation(key("villager_snore_chance"))
                .defineInRange("villagerSnoreChance", 1.0, 0, 1.0);

        displaySleepingZsOnlyWhenSnoring = builder
                .comment("Only display Z particles when a mob can snore")
                .translation(key("display_sleeping_zs_only_when_snoring"))
                .define("displaySleepingZsOnlyWhenSnoring", false);

        foxesHaveSleepingZs = builder
                .comment("Display Z particles for sleeping foxes")
                .translation(key("foxes_have_sleeping_zs"))
                .define("foxesHaveSleepingZs", true);

        adjustNametagRenderingWhenSleeping = builder
                .comment("Adjust name tag rendering to be at the top of the head rather than above it when a mob is sleeping in a bed")
                .translation(key("adjust_nametag_rendering_when_sleeping"))
                .define("adjustNametagRenderingWhenSleeping", true);

        builder.pop().translation(categoryKey("dust_clouds")).push("dustClouds");

        fallDamageDustClouds = builder
                .comment("Should a cloud of dust appear when a mob takes fall damage")
                .translation(key("fall_damage_dust_clouds"))
                .define("fallDamageDustClouds", true);

        sprintingDustClouds = builder
                .comment("Should a dust cloud form behind a sprinting player")
                .translation(key("sprinting_dust_clouds"))
                .define("sprintingDustClouds", true);

        mobSprintingDustClouds = builder
                .comment("Should a dust cloud form behind charging ravagers, galloping horses, and dashing camels")
                .translation(key("mob_sprinting_dust_clouds"))
                .define("mobSprintingDustClouds", true);

        builder.pop();

        chickenHitFeathers = builder
                .comment("When a chicken takes damage from a mob or player feathers fly off")
                .translation(key("chicken_hit_feathers"))
                .define("chickenHitFeathers", true);

        parrotHitFeathers = builder
                .comment("When a parrot takes damage from a mob or player feathers fly off")
                .translation(key("parrot_hit_feathers"))
                .define("parrotHitFeathers", true);

        enderPearlTrail = builder
                .translation(key("ender_pearl_trail"))
                .define("enderPearlTrail", true);

        snowballTrailChance = builder
                .comment("A per tick percentage chance for a snowball to spawn snowflake particles creating a trail", TO_DISABLE)
                .translation(key("snowball_trail_chance"))
                .defineInRange("snowballTrailChance", 0.2, 0, 1);

        allayMagicChance = builder
                .comment("A per tick percentage chance for particles to spawn around an allay", TO_DISABLE)
                .translation(key("allay_magic_chance"))
                .defineInRange("allayMagiChance", 0.2, 0, 1.0);

        builder.pop();

        enableSleepingZs = builder
                .comment("When an mob is sleeping display Z particles")
                .translation(key("enable_sleeping_zs"))
                .define("enableSleepingZs", true);
    }

    private static String key(String path) {
        return "config." + AmbientSleep.MOD_ID + "." + path;
    }

    private static String categoryKey(String path) {
        return "config.category." + AmbientSleep.MOD_ID + "." + path;
    }
}

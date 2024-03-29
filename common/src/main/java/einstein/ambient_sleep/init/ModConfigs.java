package einstein.ambient_sleep.init;

import einstein.ambient_sleep.AmbientSleep;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class ModConfigs {

    private static final String TO_DISABLE = "0 to disable";
    private static final List<String> DEFAULT_FALLING_BLOCK_DUST_BLOCKS = Util.make(new ArrayList<>(), list -> {
        list.add("minecraft:sand");
        list.add("minecraft:red_sand");
        list.add("minecraft:gravel");

        for (DyeColor color : DyeColor.values()) {
            list.add("minecraft:" + color.getName() + "_concrete_powder");
        }
    });

    private static final Pair<ModConfigs, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    public static final ModConfigs INSTANCE = SPEC_PAIR.getLeft();
    public static final ForgeConfigSpec SPEC = SPEC_PAIR.getRight();

    // Sparks
    public final ForgeConfigSpec.BooleanValue removeVanillaCampfireSparks;
    public final ForgeConfigSpec.BooleanValue candleSparks;
    public final ForgeConfigSpec.BooleanValue furnaceSparks;
    public final ForgeConfigSpec.BooleanValue fireSparks;
    public final ForgeConfigSpec.BooleanValue campfireSparks;
    public final ForgeConfigSpec.BooleanValue torchSparks;
    public final ForgeConfigSpec.BooleanValue lanternSparks;
    public final ForgeConfigSpec.EnumValue<LavaSparksSpawnType> lavaSparks;
    public final ForgeConfigSpec.BooleanValue lavaCauldronSparks;

    // Updated Smoke
    public final ForgeConfigSpec.BooleanValue candleSmoke;
    public final ForgeConfigSpec.BooleanValue furnaceSmoke;
    public final ForgeConfigSpec.BooleanValue fireSmoke;
    public final ForgeConfigSpec.BooleanValue torchSmoke;
    public final ForgeConfigSpec.BooleanValue lavaSparkSmoke;

    // Blocks
    public final ForgeConfigSpec.BooleanValue redstoneBlockDust;
    public final ForgeConfigSpec.EnumValue<GlowstoneDustSpawnType> glowstoneBlockDust;
    public final ForgeConfigSpec.BooleanValue beehivesHaveSleepingZs;
    public final ForgeConfigSpec.BooleanValue fallingBlockDust;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> fallingBlockDustBlocks;
    public final ForgeConfigSpec.EnumValue<SmokeType> torchflowerSmoke;
    public final ForgeConfigSpec.BooleanValue torchflowerFlames;
    public final ForgeConfigSpec.BooleanValue dragonEggParticles;
    public final ForgeConfigSpec.BooleanValue replaceEndPortalParticles;
    public final ForgeConfigSpec.BooleanValue axeStripParticles;
    public final ForgeConfigSpec.BooleanValue pumpkinCarvedParticles;
    public final ForgeConfigSpec.BooleanValue anvilBreakParticles;
    public final ForgeConfigSpec.BooleanValue anvilUseParticles;
    public final ForgeConfigSpec.BooleanValue grindstoneUseParticles;
    public final ForgeConfigSpec.ConfigValue<CommandBlockSpawnType> commandBlockParticles;
    public final ForgeConfigSpec.BooleanValue slimeBlockBounceSounds;

    // Entity Snoring
    public final ForgeConfigSpec.DoubleValue playerSnoreChance;
    public final ForgeConfigSpec.DoubleValue villagerSnoreChance;
    public final ForgeConfigSpec.BooleanValue displaySleepingZsOnlyWhenSnoring;
    public final ForgeConfigSpec.BooleanValue foxesHaveSleepingZs;
    public final ForgeConfigSpec.BooleanValue adjustNametagWhenSleeping;

    // Entity Dust Clouds
    public final ForgeConfigSpec.BooleanValue fallDamageDustClouds;
    public final ForgeConfigSpec.BooleanValue sprintingDustClouds;
    public final ForgeConfigSpec.BooleanValue mobSprintingDustClouds;

    // Burning Entities
    public final ForgeConfigSpec.EnumValue<SmokeType> burningEntitySmoke;
    public final ForgeConfigSpec.BooleanValue burningEntityFlames;
    public final ForgeConfigSpec.BooleanValue burningEntitySparks;

    // Entities
    public final ForgeConfigSpec.BooleanValue chickenHitFeathers;
    public final ForgeConfigSpec.BooleanValue parrotHitFeathers;
    public final ForgeConfigSpec.BooleanValue enderPearlTrail;
    public final ForgeConfigSpec.DoubleValue snowballTrailDensity;
    public final ForgeConfigSpec.DoubleValue allayMagicDensity;
    public final ForgeConfigSpec.BooleanValue stomachGrowling;
    public final ForgeConfigSpec.BooleanValue snowGolemHitSnowflakes;
    public final ForgeConfigSpec.BooleanValue sheepShearFluff;
    public final ForgeConfigSpec.BooleanValue improvedDragonFireballTrail;
    public final ForgeConfigSpec.ConfigValue<CommandBlockSpawnType> commandBlockMinecartParticles;
    public final ForgeConfigSpec.ConfigValue<ItemRaritySpawnType> itemRarityParticles;

    // Biomes
    public final ForgeConfigSpec.IntValue biomeParticlesRadius;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> mushroomSporeBiomes;
    public final ForgeConfigSpec.IntValue mushroomSporeDensity;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireflyBiomes;
    public final ForgeConfigSpec.IntValue fireflyDensity;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> pollenBiomes;
    public final ForgeConfigSpec.IntValue pollenDensity;

    // General
    public final ForgeConfigSpec.BooleanValue enableSleepingZs;

    public ModConfigs(ForgeConfigSpec.Builder builder) {
        builder.translation(categoryKey("blocks")).push("blocks")
                .translation(categoryKey("sparks")).push("sparks");

        removeVanillaCampfireSparks = builder
                .comment("Removes the vanilla lava spark particle from campfires")
                .translation(key("remove_vanilla_campfire_sparks"))
                .define("removeVanillaCampfireSparks", true);

        candleSparks = builder
                .translation(key("candle_sparks"))
                .define("candleSparks", true);

        furnaceSparks = builder
                .translation(key("furnace_sparks"))
                .define("furnaceSparks", true);

        fireSparks = builder
                .translation(key("fire_sparks"))
                .define("fireSparks", true);

        campfireSparks = builder
                .translation(key("campfire_sparks"))
                .define("campfireSparks", true);

        torchSparks = builder
                .translation(key("torch_sparks"))
                .define("torchSparks", true);

        lanternSparks = builder
                .translation(key("lantern_sparks"))
                .define("lanternSparks", true);

        lavaSparks = builder
                .comment("WARNING! Setting the value to ON can cause severe lag in the nether for slower PCs")
                .translation(key("lava_sparks"))
                .defineEnum("lavaSparks", LavaSparksSpawnType.ON);

        lavaCauldronSparks = builder
                .translation(key("lava_cauldron_sparks"))
                .define("lavaCauldronSparks", true);

        builder.pop()
                .comment("Replace the old black and gray smoke particles with new campfire style particles")
                .translation(categoryKey("update_smoke"))
                .push("updateSmoke");

        candleSmoke = builder
                .translation("candle_smoke")
                .define("candleSmoke", true);

        furnaceSmoke = builder
                .translation("furnace_smoke")
                .define("furnaceSmoke", true);

        fireSmoke = builder
                .translation("fire_smoke")
                .define("fireSmoke", true);

        torchSmoke = builder
                .translation("torch_smoke")
                .define("torchSmoke", true);

        lavaSparkSmoke = builder
                .translation("lava_spark_smoke")
                .define("lavaSparkSmoke", true);

        builder.pop();

        redstoneBlockDust = builder
                .translation(key("redstone_block_dust"))
                .define("redstoneBlockDust", true);

        glowstoneBlockDust = builder
                .translation(key("glowstone_block_dust"))
                .defineEnum("glowstoneBlockDust", GlowstoneDustSpawnType.ON);

        beehivesHaveSleepingZs = builder
                .comment("Display Z particles in front of bee hives/nests at night")
                .translation(key("beehives_have_sleeping_zs"))
                .define("beehivesHaveSleepingZs", true);

        fallingBlockDust = builder
                .comment("Do falling blocks have a trail of dust")
                .translation(key("falling_block_dust"))
                .define("fallingBlockDust", true);

        fallingBlockDustBlocks = builder
                .comment("A list of blocks that have a dust trail when falling")
                .translation(key("falling_block_dust_blocks"))
                .defineList("fallingBlockDustBlocks", DEFAULT_FALLING_BLOCK_DUST_BLOCKS, ModConfigs::isValidLoc);

        torchflowerSmoke = builder
                .comment("Should torchflowers have smoke particles")
                .translation(key("torchflower_smoke"))
                .defineEnum("torchflowerSmoke", SmokeType.DEFAULT);

        torchflowerFlames = builder
                .comment("Should torchflowers have flame particles")
                .translation(key("torchflower_flames"))
                .define("torchflowerFlames", true);

        dragonEggParticles = builder
                .comment("Should the dragon egg have particles like the ender chest")
                .translation(key("dragon_egg_particles"))
                .define("dragonEggParticles", true);

        replaceEndPortalParticles = builder
                .comment("Replaces the normal lit end portal smoke particles with nether portal particles")
                .translation(key("replace_end_portal_particles"))
                .define("replaceEndPortalParticles", true);

        axeStripParticles = builder
                .comment("Display the destroy particle effect when stripping a block with an axe", "This doesn't affect removing oxidization/wax from copper")
                .translation(key("axe_strip_particles"))
                .define("axeStripParticles", true);

        pumpkinCarvedParticles = builder
                .comment("Display the destroy particle effect when carving a pumpkin")
                .translation(key("pumpkin_carved_particles"))
                .define("pumpkinCarvedParticles", true);

        anvilBreakParticles = builder
                .comment("Display the destroy particle effect when an anvil breaks")
                .translation(key("anvil_break_particles"))
                .define("anvilBreakParticles", true);

        anvilUseParticles = builder
                .comment("Should spark particles fly off anvils when used")
                .translation(key("anvil_use_particles"))
                .define("anvilUseParticles", true);

        grindstoneUseParticles = builder
                .comment("Should spark particles fly off grindstones when used")
                .translation(key("grindstone_use_particles"))
                .define("grindstoneUseParticles", true);

        commandBlockParticles = builder
                .comment("Should command blocks have particles like in MC Story Mode")
                .translation(key("command_block_particles"))
                .defineEnum("commandBlockParticles", CommandBlockSpawnType.ON);

        slimeBlockBounceSounds = builder
                .comment("Slime bounce sounds when bouncing on a slime block")
                .translation(key("slime_block_bounce_sounds"))
                .define("slimeBlockBounceSounds", true);

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

        adjustNametagWhenSleeping = builder
                .comment("Adjust name tag rendering to be at the top of the head rather than above it when a mob is sleeping in a bed")
                .translation(key("adjust_nametag_when_sleeping"))
                .define("adjustNametagWhenSleeping", true);

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

        builder.pop()
                .comment("Particles emitted from entities on fire")
                .translation(categoryKey("burning_entities"))
                .push("burningEntities");

        burningEntitySmoke = builder
                .translation(key("burning_entity_smoke"))
                .defineEnum("smoke", SmokeType.DEFAULT);

        burningEntityFlames = builder
                .translation(key("burning_entity_flames"))
                .define("flames", true);

        burningEntitySparks = builder
                .translation(key("burning_entity_sparks"))
                .define("sparks", true);

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

        snowballTrailDensity = builder
                .comment("The density of the snowball particle trail", TO_DISABLE)
                .translation(key("snowball_trail_chance"))
                .defineInRange("snowballTrailChance", 0.2, 0, 1);

        allayMagicDensity = builder
                .comment("The density of particles spawning around an allay", TO_DISABLE)
                .translation(key("allay_magic_density"))
                .defineInRange("allayMagicDensity", 0.2, 0, 1.0);

        stomachGrowling = builder
                .comment("Should a stomach growl sound play every 15 sec when the player is below 3 food points (or 6 half points)")
                .translation(key("stomach_growling"))
                .define("stomachGrowling", true);

        snowGolemHitSnowflakes = builder
                .comment("When a snow golem takes damage from a mob or player snowflakes fly off")
                .translation(key("snow_golem_hit_snowflakes"))
                .define("snowGolemHitSnowflakes", true);

        sheepShearFluff = builder
                .comment("When a sheep is sheared fluff particles will fall off")
                .translation(key("sheep_shear_fluff"))
                .define("sheepShearFluff", true);

        improvedDragonFireballTrail = builder
                .comment("Adds more particles to the trail of the Ender Dragon's fireballs")
                .translation(key("improved_dragon_fireball_trail"))
                .define("improvedDragonFireballTrail", true);

        commandBlockMinecartParticles = builder
                .comment("Adds command block particles to the command block minecart. Yes I actually did this")
                .translation(key("command_block_minecart_particles"))
                .defineEnum("commandBlockMinecartParticles", CommandBlockSpawnType.ON);

        itemRarityParticles = builder
                .comment("Adds colored particles that float up from items on the ground")
                .translation(key("item_rarity_particles"))
                .defineEnum("itemRarityParticles", ItemRaritySpawnType.ON);

        builder.pop().translation(categoryKey("biomes")).push("biomes");

        biomeParticlesRadius = builder
                .comment("The radius around the player that biome particles will spawn in", TO_DISABLE)
                .translation(key("biome_particles_radius"))
                .defineInRange("biomeParticleRadius", 32, 0, 48);

        mushroomSporeBiomes = defineLocationList(
                builder.comment("A list of biome IDs that mushroom spore particles will spawn in")
                        .translation(key("mushroom_spore_biomes")),
                "mushroomSporeBiomes",
                "minecraft:mushroom_fields"
        );

        mushroomSporeDensity = builder
                .comment("The density of spawned mushroom spores in a biome", TO_DISABLE)
                .translation(key("mushroom_spore_density"))
                .defineInRange("mushroomSporeDensity", 10, 0, 100);

        fireflyBiomes = defineLocationList(
                builder.comment("A list of biome IDs that firefly particles will spawn in")
                        .translation(key("firefly_biomes")),
                "fireflyBiomes",
                "minecraft:swamp", "minecraft:mangrove_swamp"
        );

        fireflyDensity = builder
                .comment("The density of spawned fireflies in a biome", TO_DISABLE)
                .translation(key("firefly_density"))
                .defineInRange("fireflyDensity", 6, 0, 100);

        pollenBiomes = defineLocationList(
                builder.comment("A list of biome IDs that pollen particles will spawn in")
                        .translation(key("pollen_biomes")),
                "pollenBiomes",
                "minecraft:flower_forest", "minecraft:sunflower_plains"
        );

        pollenDensity = builder
                .comment("The density of spawn pollen in a biome", TO_DISABLE)
                .translation(key("pollen_density"))
                .defineInRange("pollenDensity", 50, 0, 100);

        builder.pop();

        enableSleepingZs = builder
                .comment("When an mob is sleeping display Z particles")
                .translation(key("enable_sleeping_zs"))
                .define("enableSleepingZs", true);
    }

    private ForgeConfigSpec.ConfigValue<List<? extends String>> defineLocationList(ForgeConfigSpec.Builder builder, String path, String... defaultValues) {
        return builder.defineListAllowEmpty(List.of(path), () -> List.of(defaultValues), ModConfigs::isValidLoc);
    }

    private static boolean isValidLoc(Object object) {
        if (object instanceof String string) {
            string = string.toLowerCase(Locale.ROOT);
            return ResourceLocation.tryParse(string) != null;
        }
        return false;
    }

    private static String key(String path) {
        return "config." + AmbientSleep.MOD_ID + "." + path;
    }

    private static String categoryKey(String path) {
        return "config.category." + AmbientSleep.MOD_ID + "." + path;
    }

    public enum GlowstoneDustSpawnType {
        ON,
        OFF,
        NETHER_ONLY
    }

    public enum LavaSparksSpawnType {
        ON,
        OFF,
        NOT_NETHER
    }

    public enum SmokeType {
        OFF(null),
        DEFAULT(() -> ParticleTypes.SMOKE),
        UPDATED(ModParticles.SMOKE);

        @Nullable
        private final Supplier<? extends ParticleOptions> particle;

        SmokeType(@Nullable Supplier<? extends ParticleOptions> particle) {
            this.particle = particle;
        }

        @Nullable
        public Supplier<? extends ParticleOptions> getParticle() {
            return particle;
        }
    }

    public enum CommandBlockSpawnType {
        ON,
        OFF,
        NOT_CREATIVE
    }

    public enum ItemRaritySpawnType {
        ON,
        OFF,
        NOT_COMMON
    }
}

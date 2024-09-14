package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.common.ModConfigSpec;
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

    private static final Pair<ModConfigs, ModConfigSpec> SPEC_PAIR = new ModConfigSpec.Builder().configure(ModConfigs::new);
    public static final ModConfigs INSTANCE = SPEC_PAIR.getLeft();
    public static final ModConfigSpec SPEC = SPEC_PAIR.getRight();

    // Sparks
    public final ModConfigSpec.BooleanValue removeVanillaCampfireSparks;
    public final ModConfigSpec.BooleanValue candleSparks;
    public final ModConfigSpec.BooleanValue furnaceSparks;
    public final ModConfigSpec.BooleanValue fireSparks;
    public final ModConfigSpec.BooleanValue campfireSparks;
    public final ModConfigSpec.BooleanValue torchSparks;
    public final ModConfigSpec.BooleanValue lanternSparks;
    public final ModConfigSpec.EnumValue<LavaSparksSpawnType> lavaSparks;
    public final ModConfigSpec.BooleanValue lavaCauldronSparks;

    // Updated Smoke
    public final ModConfigSpec.BooleanValue candleSmoke;
    public final ModConfigSpec.BooleanValue furnaceSmoke;
    public final ModConfigSpec.BooleanValue fireSmoke;
    public final ModConfigSpec.BooleanValue torchSmoke;
    public final ModConfigSpec.BooleanValue lavaSparkSmoke;

    // Steam
    public final ModConfigSpec.BooleanValue lavaFizzSteam;
    public final ModConfigSpec.BooleanValue steamingWater;
    public final ModConfigSpec.BooleanValue boilingWater;
    public final ModConfigSpec.BooleanValue steamingWaterCauldron;
    public final ModConfigSpec.BooleanValue boilingWaterCauldron;

    // Blocks
    public final ModConfigSpec.BooleanValue redstoneBlockDust;
    public final ModConfigSpec.EnumValue<GlowstoneDustSpawnType> glowstoneBlockDust;
    public final ModConfigSpec.BooleanValue beehivesHaveSleepingZs;
    public final ModConfigSpec.BooleanValue fallingBlockDust;
    public final ModConfigSpec.ConfigValue<List<? extends String>> fallingBlockDustBlocks;
    public final ModConfigSpec.EnumValue<SmokeType> torchflowerSmoke;
    public final ModConfigSpec.BooleanValue torchflowerFlames;
    public final ModConfigSpec.BooleanValue dragonEggParticles;
    public final ModConfigSpec.BooleanValue replaceEndPortalSmoke;
    public final ModConfigSpec.BooleanValue pumpkinCarvedParticles;
    public final ModConfigSpec.BooleanValue anvilBreakParticles;
    public final ModConfigSpec.BooleanValue anvilUseParticles;
    public final ModConfigSpec.BooleanValue grindstoneUseParticles;
    public final ModConfigSpec.ConfigValue<CommandBlockSpawnType> commandBlockParticles;
    public final ModConfigSpec.BooleanValue slimeBlockBounceSounds;
    public final ModConfigSpec.BooleanValue beaconParticles;
    public final ModConfigSpec.BooleanValue compostingParticles;
    public final ModConfigSpec.BooleanValue respawnAnchorParticles;
    public final ModConfigSpec.BooleanValue beehiveShearParticles;
    public final ModConfigSpec.BooleanValue endPortalParticles;
    public final ModConfigSpec.BooleanValue leavesDecayEffects;

    // Entity Snoring
    public final ModConfigSpec.DoubleValue playerSnoreChance;
    public final ModConfigSpec.DoubleValue villagerSnoreChance;
    public final ModConfigSpec.BooleanValue displaySleepingZsOnlyWhenSnoring;
    public final ModConfigSpec.BooleanValue foxesHaveSleepingZs;
    public final ModConfigSpec.BooleanValue adjustNameTagWhenSleeping;
    public final ModConfigSpec.BooleanValue sleepingZs;

    // Entity Dust Clouds
    public final ModConfigSpec.BooleanValue fallDamageDustClouds;
    public final ModConfigSpec.BooleanValue sprintingDustClouds;
    public final ModConfigSpec.BooleanValue mobSprintingDustClouds;

    // Burning Entities
    public final ModConfigSpec.EnumValue<SmokeType> burningEntitySmoke;
    public final ModConfigSpec.BooleanValue burningEntityFlames;
    public final ModConfigSpec.BooleanValue burningEntitySparks;
    public final ModConfigSpec.BooleanValue burningEntitySounds;

    // Item Rarity
    public final ModConfigSpec.ConfigValue<ItemRaritySpawnType> itemRarityParticles;
    public final ModConfigSpec.ConfigValue<ItemRarityColorType> itemRarityParticleColor;
    public final ModConfigSpec.IntValue itemRarityParticleHeight;

    // Entities
    public final ModConfigSpec.BooleanValue chickenHitFeathers;
    public final ModConfigSpec.BooleanValue parrotHitFeathers;
    public final ModConfigSpec.BooleanValue enderPearlTrail;
    public final ModConfigSpec.DoubleValue snowballTrailDensity;
    public final ModConfigSpec.DoubleValue allayMagicDensity;
    public final ModConfigSpec.DoubleValue vexMagicDensity;
    public final ModConfigSpec.BooleanValue stomachGrowling;
    public final ModConfigSpec.BooleanValue snowGolemHitSnowflakes;
    public final ModConfigSpec.BooleanValue sheepShearFluff;
    public final ModConfigSpec.BooleanValue improvedDragonFireballTrail;
    public final ModConfigSpec.ConfigValue<CommandBlockSpawnType> commandBlockMinecartParticles;
    public final ModConfigSpec.BooleanValue heartBeating;
    public final ModConfigSpec.BooleanValue updateLitTntSmoke;
    public final ModConfigSpec.BooleanValue litTntFlames;
    public final ModConfigSpec.BooleanValue litTntSparks;
    public final ModConfigSpec.BooleanValue endCrystalParticles;
    public final ModConfigSpec.BooleanValue minecartLandingSparks;
    public final ModConfigSpec.BooleanValue slimeTrails;
    public final ModConfigSpec.BooleanValue magmaCubeTrails;
    public final ModConfigSpec.BooleanValue replaceSlimeSquishParticles;
    public final ModConfigSpec.BooleanValue replaceSpellCasterParticles;
    public final ModConfigSpec.BooleanValue ironGolemCrackParticles;

    // Biomes
    public final ModConfigSpec.IntValue biomeParticlesRadius;
    public final ModConfigSpec.ConfigValue<List<? extends String>> mushroomSporeBiomes;
    public final ModConfigSpec.IntValue mushroomSporeDensity;
    public final ModConfigSpec.ConfigValue<List<? extends String>> fireflyBiomes;
    public final ModConfigSpec.IntValue fireflyDensity;
    public final ModConfigSpec.ConfigValue<List<? extends String>> pollenBiomes;
    public final ModConfigSpec.IntValue pollenDensity;
    public final ModConfigSpec.ConfigValue<List<? extends String>> sculkDustBiomes;
    public final ModConfigSpec.IntValue sculkDustDensity;

    // Items
    public final ModConfigSpec.BooleanValue axeStripParticles;
    public final ModConfigSpec.BooleanValue waterEvaporateFromBucketSteam;
    public final ModConfigSpec.BooleanValue boneMealUsingParticles;

    // General
    public final ModConfigSpec.BooleanValue mobSkullShaders;
    public final ModConfigSpec.IntValue particleRenderDistance;

    public ModConfigs(ModConfigSpec.Builder builder) {
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
                .comment("WARNING! This can cause severe lag for slower computers when around large pools of lava (like in the nether)")
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
                .comment("Includes vanilla furnace variants")
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

        builder.pop()
                .translation(categoryKey("steam"))
                .push("steam");

        lavaFizzSteam = builder
                .comment("Replaces the smoke particles with steam when lava and water create a block (obsidian/cobblestone/stone)")
                .translation(key("lava_fizz_steam"))
                .define("lavaFizzSteam", true);

        steamingWater = builder
                .comment("Should water steam when near blocks with a light level greater than 11")
                .translation(key("steaming_water"))
                .define("steamingWater", false);

        boilingWater = builder
                .comment("Should water bubble/boil when near blocks with a light level of 13 or more")
                .translation(key("boiling_water"))
                .define("boilingWater", false);

        steamingWaterCauldron = builder
                .comment("Should water cauldrons steam when near blocks with a light level greater than 11")
                .translation(key("steaming_water_cauldron"))
                .define("steamingWaterCauldron", false);

        boilingWaterCauldron = builder
                .comment("Should water bubble/boil when near blocks with a light level of 13 or more")
                .translation(key("boiling_water_cauldron"))
                .define("boilingWaterCauldron", false);

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
                .defineList("fallingBlockDustBlocks", DEFAULT_FALLING_BLOCK_DUST_BLOCKS, () -> "", ModConfigs::isValidLoc);

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

        replaceEndPortalSmoke = builder
                .comment("Replaces the normal end portal smoke particles with nether portal particles so that it is consistent with end gateways")
                .translation(key("replace_end_portal_smoke"))
                .define("replaceEndPortalSmoke", true);

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

        beaconParticles = builder
                .comment("Should beacons emit particles that float up",
                        "WARNING! This can cause severe lag for slower computers when around a beacon, potentially adding up to a couple of thousand particles at one time per beacon")
                .translation(key("beacon_particles"))
                .define("beaconParticles", true);

        compostingParticles = builder
                .comment("Compost particles when putting items in a composter")
                .translation(key("composting_particles"))
                .define("compostingParticles", true);

        respawnAnchorParticles = builder
                .comment("Crying obsidian particles for respawn anchors")
                .translation(key("respawn_anchor_particles"))
                .define("respawnAnchorParticles", true);

        beehiveShearParticles = builder
                .comment("Display the destroy particle effect when shearing a beehive/nest")
                .translation(key("beehive_shear_particles"))
                .define("beehiveShearParticles", true);

        endPortalParticles = builder
                .comment("Should end portals and end gateways have little colored particles floating around them")
                .translation(key("end_portal_particles"))
                .define("endPortalParticles", true);

        leavesDecayEffects = builder
                .comment("Play the destroy block sound and particle effects when leaves decay/despawn")
                .translation(key("leaves_decay_effects"))
                .define("leavesDecayEffects", true);

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

        adjustNameTagWhenSleeping = builder
                .comment("Adjust name tag rendering to be at the top of the head rather than above it when a mob is sleeping in a bed")
                .translation(key("adjust_name_tag_when_sleeping"))
                .define("adjustNameTagWhenSleeping", true);

        sleepingZs = builder
                .comment("When a mob is sleeping display Z particles")
                .translation(key("sleeping_zs"))
                .define("sleepingZs", true);

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

        burningEntitySounds = builder
                .comment("Should burning randomly play the fire extinguish sound")
                .translation(key("burning_entity_sounds"))
                .define("sounds", true);

        builder.pop().translation(categoryKey("item_rarity")).push("itemRarity");

        itemRarityParticles = builder
                .comment("Adds colored particles that float up from items on the ground")
                .translation(key("item_rarity_particles"))
                .defineEnum("itemRarityParticles", ItemRaritySpawnType.ON);

        itemRarityParticleColor = builder
                .comment("How the item rarity particles are colored")
                .translation(key("item_rarity_particle_color"))
                .defineEnum("itemRarityParticleColor", ItemRarityColorType.NAME_COLOR);

        itemRarityParticleHeight = builder
                .comment("How high item rarity particles float")
                .translation(key("item_rarity_particle_height"))
                .defineInRange("itemRarityParticleHeight", 7, 3, 15);

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

        vexMagicDensity = builder
                .comment("The density of particles spawning around a vex", TO_DISABLE)
                .translation(key("vex_magic_density"))
                .defineInRange("vexMagicDensity", 0.2, 0, 1.0);

        stomachGrowling = builder
                .comment("Should a stomach growl sound play every 15 sec when the player's is below 3 food points (or 6 half points)")
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

        heartBeating = builder
                .comment("Should a heartbeat sound play every 3 sec when the player's health is below 3 health points (or 6 half points) and speed up below 2 health points")
                .translation(key("heart_beating"))
                .define("heartBeating", true);

        updateLitTntSmoke = builder
                .translation(key("update_lit_tnt_smoke"))
                .define("updateLitTntSmoke", true);

        litTntFlames = builder
                .translation(key("lit_tnt_flames"))
                .define("litTntFlames", true);

        litTntSparks = builder
                .translation(key("lit_tnt_sparks"))
                .define("litTntSparks", true);

        endCrystalParticles = builder
                .comment("Should end crystals have little pink particles floating around them")
                .translation("end_crystal_particles")
                .define("endCrystalParticles", true);

        minecartLandingSparks = builder
                .comment("Should sparks particles spawn when a falling minecart lands on rails")
                .translation(key("minecart_landing_sparks"))
                .define("minecartLandingSparks", true);

        slimeTrails = builder
                .comment("Do Slimes leave a trail of slime behind them when they move")
                .translation(key("slime_trails"))
                .define("slimeTrails", true);

        magmaCubeTrails = builder
                .comment("Do Magma Cubes leave a trail of maga slime behind them when they move")
                .translation(key("magma_cube_trails"))
                .define("magmaCubeTrails", true);

        replaceSlimeSquishParticles = builder
                .comment("Replaces the slime ball item particles with slime block particles when a slime squishes")
                .translation(key("replace_slime_squish_particles"))
                .define("replaceSlimeSquishParticles", true);

        replaceSpellCasterParticles = builder
                .comment("Replaces the potion effect particles when a spell caster illager is casting a spell e.g Evoker summoning vexes")
                .translation(key("replace_spell_caster_particles"))
                .define("replaceSpellCasterParticles", true);

        ironGolemCrackParticles = builder
                .comment("Should iron particles spawn around iron golems when they crack")
                .translation(key("iron_golem_crack_particles"))
                .define("ironGolemCrackParticles", true);

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
                .comment("The density of spawned pollen in a biome", TO_DISABLE)
                .translation(key("pollen_density"))
                .defineInRange("pollenDensity", 50, 0, 100);

        sculkDustBiomes = defineLocationList(
                builder.comment("A list of biomes IDs that sculk dust particles will spawn in")
                        .translation(key("sculk_dust_biomes")),
                "sculkDustBiomes",
                "minecraft:deep_dark"
        );

        sculkDustDensity = builder
                .comment("The density of spawned sculk dust in a biome", TO_DISABLE)
                .translation(key("sculk_dust_density"))
                .defineInRange("sculkDustDensity", 5, 0, 100);

        builder.pop().translation(categoryKey("items")).push("items");

        axeStripParticles = builder
                .comment("Display the destroy particle effect when stripping a block with an axe", "This doesn't affect removing oxidization/wax from copper")
                .translation(key("axe_strip_particles"))
                .define("axeStripParticles", true);

        waterEvaporateFromBucketSteam = builder
                .comment("Replaces the smoke particles with steam when water evaporates from a bucket in the nether")
                .translation(key("water_evaporate_from_bucket_steam"))
                .define("waterEvaporateFromBucketSteam", true);

        boneMealUsingParticles = builder
                .comment("Should bone meal item particles spawn when using bone meal")
                .translation(key("bone_meal_using_particles"))
                .define("boneMealUsingParticles", true);

        builder.pop();

        mobSkullShaders = builder
                .comment("Apply a mob's spectate shader when wearing its skull/head e.g Creeper head makes everything look green", "No new shaders are added")
                .translation(key("mob_skull_shaders"))
                .define("mobSkullShaders", true);

        particleRenderDistance = builder
                .comment("The maximum number of chunks away from the player a particle will render.",
                        "This does not affect particles spawning/existing in the world only rendering.",
                        "This affects most particles in the game including particles from other mods, some particles however ignore this value e.g. campfire smoke.")
                .translation("particle_render_distance")
                .defineInRange("particleRenderDistance", 5, 1, 32);
    }

    private ModConfigSpec.ConfigValue<List<? extends String>> defineLocationList(ModConfigSpec.Builder builder, String path, String... defaultValues) {
        return builder.defineListAllowEmpty(List.of(path), () -> List.of(defaultValues), () -> "", ModConfigs::isValidLoc);
    }

    private static boolean isValidLoc(Object object) {
        if (object instanceof String string) {
            string = string.toLowerCase(Locale.ROOT);
            return ResourceLocation.tryParse(string) != null;
        }
        return false;
    }

    private static String key(String path) {
        return "config." + SubtleEffects.MOD_ID + "." + path;
    }

    private static String categoryKey(String path) {
        return "config.category." + SubtleEffects.MOD_ID + "." + path;
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

    public enum ItemRarityColorType {
        RARITY_COLOR,
        NAME_COLOR
    }
}

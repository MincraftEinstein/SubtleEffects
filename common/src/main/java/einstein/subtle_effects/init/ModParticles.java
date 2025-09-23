package einstein.subtle_effects.init;

import com.mojang.serialization.MapCodec;
import einstein.subtle_effects.particle.*;
import einstein.subtle_effects.particle.emitter.FireFlyEmitter;
import einstein.subtle_effects.particle.emitter.PotionEmitter;
import einstein.subtle_effects.particle.emitter.SplashEmitter;
import einstein.subtle_effects.particle.option.*;
import einstein.subtle_effects.particle.provider.*;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.core.particles.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = register("snoring", SnoringParticle.Provider::new);
    public static final Supplier<SimpleParticleType> FALLING_SNORING = register("falling_snoring", SnoringParticle.FallingProvider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> LONG_SPARK = register("long_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec, SparkParticle.LongLifeProvider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> SHORT_SPARK = register("short_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec, SparkParticle.ShortLifeProvider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> FLOATING_SPARK = register("floating_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec, SparkParticle.FloatingProvider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> METAL_SPARK = register("metal_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec, SparkParticle.MetalProvider::new);
    public static final Supplier<SimpleParticleType> SNOW = register("snow", SnowParticle.Provider::new);
    public static final Supplier<SimpleParticleType> SNOWBALL_TRAIL = register("snowball_trail", SnowParticle.SnowballTrailProvider::new);
    public static final Supplier<SimpleParticleType> FREEZING = register("freezing", SnowParticle.FreezingProvider::new);
    public static final Supplier<SimpleParticleType> CHICKEN_FEATHER = register("chicken_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> BLUE_PARROT_FEATHER = register("blue_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> GRAY_PARROT_FEATHER = register("gray_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> GREEN_PARROT_FEATHER = register("green_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> RED_BLUE_PARROT_FEATHER = register("red_blue_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> YELLOW_BLUE_PARROT_FEATHER = register("yellow_blue_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> ALLAY_MAGIC = register("allay_magic", AllayMagicParticle.Provider::new);
    public static final Supplier<ParticleType<BooleanParticleOptions>> VEX_MAGIC = register("vex_magic", BooleanParticleOptions::codec, BooleanParticleOptions::streamCodec, AllayMagicParticle.VexProvider::new);
    public static final Supplier<SimpleParticleType> SMALL_DUST_CLOUD = register("small_dust_cloud", DustCloudParticle.SmallProvider::new);
    public static final Supplier<SimpleParticleType> LARGE_DUST_CLOUD = register("large_dust_cloud", DustCloudParticle.LargeProvider::new);
    public static final Supplier<ParticleType<SheepFluffParticleOptions>> SHEEP_FLUFF = register("sheep_fluff", type -> SheepFluffParticleOptions.CODEC, type -> SheepFluffParticleOptions.STREAM_CODEC, SheepFluffParticle.Provider::new);
    public static final Supplier<SimpleParticleType> MUSHROOM_SPORE = register("mushroom_spore", MushroomSporeProvider::new);
    public static final Supplier<SimpleParticleType> FIREFLY = register("firefly", FireflyParticle.Provider::new);
    public static final Supplier<SimpleParticleType> VANILLA_FIREFLY = register("vanilla_firefly", VanillaFireflyParticle.FireflyProvider::new);
    public static final Supplier<SimpleParticleType> FIREFLY_EMITTER = register("firefly_emitter", sprites -> new FireFlyEmitter.Provider());
    public static final Supplier<SimpleParticleType> SMOKE = register("smoke", SmokeParticleProvider::new);
    public static final Supplier<SimpleParticleType> POLLEN = register("pollen", PollenProvider::new);
    public static final Supplier<ParticleType<DirectionParticleOptions>> COMMAND_BLOCK = register("command_block", DirectionParticleOptions::codec, DirectionParticleOptions::streamCodec, CommandBlockParticle.Provider::new);
    public static final Supplier<ParticleType<IntegerParticleOptions>> ITEM_RARITY = register("item_rarity", IntegerParticleOptions::codec, IntegerParticleOptions::streamCodec, ItemRarityParticle.Provider::new);
    public static final Supplier<ParticleType<PositionParticleOptions>> BEACON = register("beacon", PositionParticleOptions::codec, PositionParticleOptions::streamCodec, BeaconParticle.Provider::new);
    public static final Supplier<SimpleParticleType> COMPOST = register("compost", CustomTerrainParticle.CompostProvider::new);
    public static final Supplier<SimpleParticleType> STEAM = register("steam", SteamParticle.Provider::new);
    public static final Supplier<SimpleParticleType> END_PORTAL = register("end_portal", EndPortalParticle.Provider::new);
    public static final Supplier<SimpleParticleType> END_CRYSTAL = register("end_crystal", EndCrystalParticle.Provider::new);
    public static final Supplier<SimpleParticleType> SCULK_DUST = register("sculk_dust", SculkDustParticle.Provider::new);
    public static final Supplier<ParticleType<FloatParticleOptions>> SLIME_TRAIL = register("slime_trail", FloatParticleOptions::codec, FloatParticleOptions::streamCodec, SlimeTrailParticle.Provider::new);
    public static final Supplier<ParticleType<FloatParticleOptions>> MAGMA_CUBE_TRAIL = register("magma_cube_trail", FloatParticleOptions::codec, FloatParticleOptions::streamCodec, SlimeTrailParticle.Provider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> SPELL_CASTER_MAGIC = register("spell_caster_magic", ColorParticleOption::codec, ColorParticleOption::streamCodec, SpellCasterMagicProvider::new);
    public static final Supplier<SimpleParticleType> AMETHYST_SPARKLE = register("amethyst_sparkle", SuspendedTownParticle.HappyVillagerProvider::new);
    public static final Supplier<SimpleParticleType> AZALEA_PETAL = register("azalea_petal", AzaleaParticleProvider::new);
    public static final Supplier<SimpleParticleType> FROSTY_BREATH = register("frosty_breath", SteamParticle.FrostyBreathProvider::new);
    public static final Supplier<ParticleType<FloatParticleOptions>> EXPERIENCE = register("experience", FloatParticleOptions::codec, FloatParticleOptions::streamCodec, ExperienceParticle.Provider::new);
    public static final Supplier<SimpleParticleType> HEART_POP = register("heart_pop", HeartPopParticle.Provider::new);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_RING = register("potion_ring", ColorAndIntegerParticleOptions::codec, ColorAndIntegerParticleOptions::streamCodec, PotionRingParticle.Provider::new);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_DOT = register("potion_dot", ColorAndIntegerParticleOptions::codec, ColorAndIntegerParticleOptions::streamCodec, PotionDotParticle.PotionDotProvider::new);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_EMITTER = register("potion_emitter", ColorAndIntegerParticleOptions::codec, ColorAndIntegerParticleOptions::streamCodec, sprites -> new PotionEmitter.Provider());
    public static final Supplier<SimpleParticleType> IRON_GOLEM = register("iron_golem", CustomTerrainParticle.IronGolemProvider::new);
    public static final Supplier<SimpleParticleType> DROWNING_BUBBLE = register("drowning_bubble", DrowningBubbleParticle.Provider::new);
    public static final Supplier<SimpleParticleType> DROWNING_BUBBLE_POP = register("drowning_bubble_pop", DrowningBubblePopParticle.Provider::new);
    public static final Supplier<ParticleType<DirectionParticleOptions>> EGG_SPLAT = register("egg_splat", DirectionParticleOptions::codec, DirectionParticleOptions::streamCodec, EggSplatParticle.Provider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> ENDER_EYE_PLACED_RING = register("ender_eye_placed_ring", ColorParticleOption::codec, ColorParticleOption::streamCodec, EnderEyePlacedRingParticle.Provider::new);
    public static final Supplier<ParticleType<BlockParticleOption>> BLOCK_NO_MOMENTUM = register("block_no_momentum", BlockParticleOption::codec, BlockParticleOption::streamCodec, sprites -> new TerrainNoMomentumParticleProvider());
    public static final Supplier<SimpleParticleType> OMINOUS_VAULT_CONNECTION = register("ominous_vault_connection", FlyTowardsPositionParticle.VaultConnectionProvider::new);
    public static final Supplier<ParticleType<GeyserSpoutParticleOptions>> GEYSER_SPOUT = register("geyser_spout", type -> GeyserSpoutParticleOptions.CODEC, type -> GeyserSpoutParticleOptions.STREAM_CODEC, GeyserSpoutParticle.Provider::new);
    public static final Supplier<SimpleParticleType> SNEEZE = register("sneeze", SneezeParticle.Provider::new);
    public static final Supplier<SimpleParticleType> GEYSER_SMOKE = register("geyser_smoke", GeyserSmokeParticleProvider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> POTION_CLOUD = register("potion_cloud", ColorParticleOption::codec, ColorParticleOption::streamCodec, PotionCloudParticle.Provider::new);
    public static final Supplier<ParticleType<ColorParticleOption>> POTION_POOF_CLOUD = register("potion_poof_cloud", ColorParticleOption::codec, ColorParticleOption::streamCodec, PotionPoofCloudProvider::new);
    public static final Supplier<SimpleParticleType> WATER_RIPPLE = register("water_ripple", RippleParticle.Provider::new);
    public static final Supplier<SimpleParticleType> LAVA_RIPPLE = register("lava_ripple", RippleParticle.LavaProvider::new);
    public static final Supplier<ParticleType<SplashParticleOptions>> WATER_SPLASH = register("water_splash", SplashParticleOptions::codec, SplashParticleOptions::streamCodec, SplashParticle.Provider::new);
    // TODO temporary name until other lava splash particle is removed
    public static final Supplier<ParticleType<SplashParticleOptions>> ENTITY_LAVA_SPLASH = register("entity_lava_splash", SplashParticleOptions::codec, SplashParticleOptions::streamCodec, SplashParticle.LavaProvider::new);
    public static final Supplier<ParticleType<SplashDropletParticleOptions>> WATER_SPLASH_DROPLET = register("water_splash_droplet", SplashDropletParticleOptions::codec, SplashDropletParticleOptions::streamCodec, SplashDropletParticle.Provider::new);
    public static final Supplier<ParticleType<SplashDropletParticleOptions>> LAVA_SPLASH_DROPLET = register("lava_splash_droplet", SplashDropletParticleOptions::codec, SplashDropletParticleOptions::streamCodec, SplashDropletParticle.LavaProvider::new);
    public static final Supplier<ParticleType<IntegerParticleOptions>> WATER_SPLASH_EMITTER = register("water_splash_emitter", IntegerParticleOptions::codec, IntegerParticleOptions::streamCodec, sprites -> new SplashEmitter.Provider());
    public static final Supplier<ParticleType<IntegerParticleOptions>> LAVA_SPLASH_EMITTER = register("lava_splash_emitter", IntegerParticleOptions::codec, IntegerParticleOptions::streamCodec, sprites -> new SplashEmitter.LavaProvider());
    public static final Supplier<SimpleParticleType> WATERFALL_CLOUD = register("waterfall_cloud", WaterfallCloud.Provider::new);
    public static final Supplier<SimpleParticleType> WATERFALL_DROPLET = register("waterfall_droplet", WaterfallDropletParticle.Provider::new);
    public static final Supplier<SimpleParticleType> WATERFALL_MIST = register("waterfall_mist", WaterfallMistParticle.Provider::new);
    public static final Supplier<ParticleType<FallenLeafParticleOptions>> FALLEN_LEAF = register("fallen_leaf", type -> FallenLeafParticleOptions.CODEC, type -> FallenLeafParticleOptions.STREAM_CODEC, sprites -> new FallenLeafParticle.Provider());

    public static void init() {
    }

    private static Supplier<SimpleParticleType> register(String name, Function<SpriteSet, ParticleProvider<SimpleParticleType>> provider) {
        Supplier<SimpleParticleType> particleType = REGISTRY.registerParticle(name, () -> new SimpleParticleType(false));
        REGISTRY.registerParticleProvider(particleType, provider);
        return particleType;
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParticleType<V>, V extends ParticleOptions> Supplier<T> register(String name, Function<ParticleType<V>, MapCodec<V>> codec, Function<ParticleType<V>, StreamCodec<? super RegistryFriendlyByteBuf, V>> streamCodec, Function<SpriteSet, ParticleProvider<V>> provider) {
        Supplier<T> particleType = (Supplier<T>) REGISTRY.registerParticle(name, () -> new ParticleType<V>(false) {

            @Override
            public MapCodec<V> codec() {
                return codec.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, V> streamCodec() {
                return streamCodec.apply(this);
            }
        });
        REGISTRY.registerParticleProvider(particleType, provider);
        return particleType;
    }
}

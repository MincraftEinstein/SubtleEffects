package einstein.subtle_effects.init;

import com.mojang.serialization.Codec;
import einstein.subtle_effects.particle.option.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = register("snoring");
    public static final Supplier<SimpleParticleType> FALLING_SNORING = register("falling_snoring");
    public static final Supplier<ParticleType<ColorParticleOptions>> LONG_SPARK = register("long_spark", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<ParticleType<ColorParticleOptions>> SHORT_SPARK = register("short_spark", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<ParticleType<ColorParticleOptions>> FLOATING_SPARK = register("floating_spark", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<ParticleType<ColorParticleOptions>> METAL_SPARK = register("metal_spark", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<SimpleParticleType> SNOW = register("snow");
    public static final Supplier<SimpleParticleType> SNOWBALL_TRAIL = register("snowball_trail");
    public static final Supplier<SimpleParticleType> FREEZING = register("freezing");
    public static final Supplier<SimpleParticleType> CHICKEN_FEATHER = register("chicken_feather");
    public static final Supplier<SimpleParticleType> BLUE_PARROT_FEATHER = register("blue_parrot_feather");
    public static final Supplier<SimpleParticleType> GRAY_PARROT_FEATHER = register("gray_parrot_feather");
    public static final Supplier<SimpleParticleType> GREEN_PARROT_FEATHER = register("green_parrot_feather");
    public static final Supplier<SimpleParticleType> RED_BLUE_PARROT_FEATHER = register("red_blue_parrot_feather");
    public static final Supplier<SimpleParticleType> YELLOW_BLUE_PARROT_FEATHER = register("yellow_blue_parrot_feather");
    public static final Supplier<SimpleParticleType> ALLAY_MAGIC = register("allay_magic");
    public static final Supplier<ParticleType<BooleanParticleOptions>> VEX_MAGIC = register("vex_magic", BooleanParticleOptions.DESERIALIZER, BooleanParticleOptions::codec);
    public static final Supplier<SimpleParticleType> SMALL_DUST_CLOUD = register("small_dust_cloud");
    public static final Supplier<SimpleParticleType> LARGE_DUST_CLOUD = register("large_dust_cloud");
    public static final Supplier<ParticleType<SheepFluffParticleOptions>> SHEEP_FLUFF = register("sheep_fluff", SheepFluffParticleOptions.DESERIALIZER, type -> SheepFluffParticleOptions.CODEC);
    public static final Supplier<SimpleParticleType> MUSHROOM_SPORE = register("mushroom_spore");
    public static final Supplier<SimpleParticleType> FIREFLY = register("firefly");
    public static final Supplier<SimpleParticleType> VANILLA_FIREFLY = register("vanilla_firefly");
    public static final Supplier<SimpleParticleType> FIREFLY_EMITTER = register("firefly_emitter");
    public static final Supplier<SimpleParticleType> SMOKE = register("smoke");
    public static final Supplier<SimpleParticleType> POLLEN = register("pollen");
    public static final Supplier<ParticleType<DirectionParticleOptions>> COMMAND_BLOCK = register("command_block", DirectionParticleOptions.DESERIALIZER, DirectionParticleOptions::codec);
    public static final Supplier<ParticleType<IntegerParticleOptions>> ITEM_RARITY = register("item_rarity", IntegerParticleOptions.DESERIALIZER, IntegerParticleOptions::codec);
    public static final Supplier<ParticleType<PositionParticleOptions>> BEACON = register("beacon", PositionParticleOptions.DESERIALIZER, PositionParticleOptions::codec);
    public static final Supplier<SimpleParticleType> COMPOST = register("compost");
    public static final Supplier<SimpleParticleType> STEAM = register("steam");
    public static final Supplier<SimpleParticleType> END_PORTAL = register("end_portal");
    public static final Supplier<SimpleParticleType> END_CRYSTAL = register("end_crystal");
    public static final Supplier<SimpleParticleType> SCULK_DUST = register("sculk_dust");
    public static final Supplier<ParticleType<FloatParticleOptions>> SLIME_TRAIL = register("slime_trail", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<ParticleType<FloatParticleOptions>> MAGMA_CUBE_TRAIL = register("magma_cube_trail", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<SimpleParticleType> SPELL_CASTER_MAGIC = register("spell_caster_magic");
    public static final Supplier<SimpleParticleType> AMETHYST_SPARKLE = register("amethyst_sparkle");
    public static final Supplier<SimpleParticleType> AZALEA_PETAL = register("azalea_petal");
    public static final Supplier<SimpleParticleType> FROSTY_BREATH = register("frosty_breath");
    public static final Supplier<ParticleType<FloatParticleOptions>> EXPERIENCE = register("experience", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<SimpleParticleType> HEART_POP = register("heart_pop");
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_RING = register("potion_ring", ColorAndIntegerParticleOptions.DESERIALIZER, ColorAndIntegerParticleOptions::codec);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_DOT = register("potion_dot", ColorAndIntegerParticleOptions.DESERIALIZER, ColorAndIntegerParticleOptions::codec);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_EMITTER = register("potion_emitter", ColorAndIntegerParticleOptions.DESERIALIZER, ColorAndIntegerParticleOptions::codec);
    public static final Supplier<SimpleParticleType> IRON_GOLEM = register("iron_golem");
    public static final Supplier<SimpleParticleType> DROWNING_BUBBLE = register("drowning_bubble");
    public static final Supplier<SimpleParticleType> DROWNING_BUBBLE_POP = register("drowning_bubble_pop");
    public static final Supplier<ParticleType<DirectionParticleOptions>> EGG_SPLAT = register("egg_splat", DirectionParticleOptions.DESERIALIZER, DirectionParticleOptions::codec);
    public static final Supplier<ParticleType<ColorParticleOptions>> ENDER_EYE_PLACED_RING = register("ender_eye_placed_ring", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<ParticleType<BlockParticleOption>> BLOCK_NO_MOMENTUM = register("block_no_momentum", BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
    public static final Supplier<ParticleType<GeyserSpoutParticleOptions>> GEYSER_SPOUT = register("geyser_spout", GeyserSpoutParticleOptions.DESERIALIZER, type -> GeyserSpoutParticleOptions.CODEC);
    public static final Supplier<SimpleParticleType> SNEEZE = register("sneeze");
    public static final Supplier<SimpleParticleType> GEYSER_SMOKE = register("geyser_smoke");
    public static final Supplier<ParticleType<ColorParticleOptions>> POTION_CLOUD = register("potion_cloud", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<ParticleType<ColorParticleOptions>> POTION_POOF_CLOUD = register("potion_poof_cloud", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<ParticleType<FloatParticleOptions>> WATER_RIPPLE = register("water_ripple", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<ParticleType<FloatParticleOptions>> LAVA_RIPPLE = register("lava_ripple", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<ParticleType<SplashParticleOptions>> WATER_SPLASH = register("water_splash", SplashParticleOptions.DESERIALIZER, SplashParticleOptions::codec);
    public static final Supplier<ParticleType<SplashParticleOptions>> LAVA_SPLASH = register("lava_splash", SplashParticleOptions.DESERIALIZER, SplashParticleOptions::codec);
    public static final Supplier<ParticleType<FloatParticleOptions>> WATER_SPLASH_RIPPLE = register("water_splash_ripple", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<ParticleType<FloatParticleOptions>> LAVA_SPLASH_RIPPLE = register("lava_splash_ripple", FloatParticleOptions.DESERIALIZER, FloatParticleOptions::codec);
    public static final Supplier<ParticleType<SplashDropletParticleOptions>> WATER_SPLASH_DROPLET = register("water_splash_droplet", SplashDropletParticleOptions.DESERIALIZER, SplashDropletParticleOptions::codec);
    public static final Supplier<ParticleType<SplashDropletParticleOptions>> LAVA_SPLASH_DROPLET = register("lava_splash_droplet", SplashDropletParticleOptions.DESERIALIZER, SplashDropletParticleOptions::codec);
    public static final Supplier<ParticleType<SplashEmitterParticleOptions>> WATER_SPLASH_EMITTER = register("water_splash_emitter", SplashEmitterParticleOptions.DESERIALIZER, SplashEmitterParticleOptions::codec);
    public static final Supplier<ParticleType<SplashEmitterParticleOptions>> LAVA_SPLASH_EMITTER = register("lava_splash_emitter", SplashEmitterParticleOptions.DESERIALIZER, SplashEmitterParticleOptions::codec);
    public static final Supplier<SimpleParticleType> WATERFALL_CLOUD = register("waterfall_cloud");
    public static final Supplier<SimpleParticleType> WATERFALL_DROPLET = register("waterfall_droplet");
    public static final Supplier<SimpleParticleType> WATERFALL_MIST = register("waterfall_mist");
    public static final Supplier<ParticleType<FallenLeafParticleOptions>> FALLEN_LEAF = register("fallen_leaf", FallenLeafParticleOptions.DESERIALIZER, type -> FallenLeafParticleOptions.CODEC);

    public static void init() {
    }

    private static Supplier<SimpleParticleType> register(String name) {
        return REGISTRY.registerParticle(name, () -> new SimpleParticleType(false));
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private static <T extends ParticleType<V>, V extends ParticleOptions> Supplier<T> register(String name, ParticleOptions.Deserializer<V> deserializer, Function<ParticleType<V>, Codec<V>> codec) {
        return (Supplier<T>) REGISTRY.registerParticle(name, () -> new ParticleType<V>(false, deserializer) {

            @Override
            public Codec<V> codec() {
                return codec.apply(this);
            }
        });
    }
}

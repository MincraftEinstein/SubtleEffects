package einstein.subtle_effects.init;

import com.mojang.serialization.MapCodec;
import einstein.subtle_effects.particle.option.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = register("snoring");
    public static final Supplier<SimpleParticleType> FALLING_SNORING = register("falling_snoring");
    public static final Supplier<ParticleType<ColorParticleOption>> LONG_SPARK = register("long_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<ParticleType<ColorParticleOption>> SHORT_SPARK = register("short_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<ParticleType<ColorParticleOption>> FLOATING_SPARK = register("floating_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<ParticleType<ColorParticleOption>> METAL_SPARK = register("metal_spark", ColorParticleOption::codec, ColorParticleOption::streamCodec);
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
    public static final Supplier<ParticleType<BooleanParticleOptions>> VEX_MAGIC = register("vex_magic", BooleanParticleOptions::codec, BooleanParticleOptions::streamCodec);
    public static final Supplier<SimpleParticleType> SMALL_DUST_CLOUD = register("small_dust_cloud");
    public static final Supplier<SimpleParticleType> LARGE_DUST_CLOUD = register("large_dust_cloud");
    public static final Supplier<ParticleType<SheepFluffParticleOptions>> SHEEP_FLUFF = register("sheep_fluff", type -> SheepFluffParticleOptions.CODEC, type -> SheepFluffParticleOptions.STREAM_CODEC);
    public static final Supplier<SimpleParticleType> MUSHROOM_SPORE = register("mushroom_spore");
    public static final Supplier<SimpleParticleType> FIREFLY = register("firefly");
    public static final Supplier<SimpleParticleType> VANILLA_FIREFLY = register("vanilla_firefly");
    public static final Supplier<SimpleParticleType> FIREFLY_EMITTER = register("firefly_emitter");
    public static final Supplier<SimpleParticleType> SMOKE = register("smoke");
    public static final Supplier<SimpleParticleType> POLLEN = register("pollen");
    public static final Supplier<ParticleType<DirectionParticleOptions>> COMMAND_BLOCK = register("command_block", DirectionParticleOptions::codec, DirectionParticleOptions::streamCodec);
    public static final Supplier<ParticleType<IntegerParticleOptions>> ITEM_RARITY = register("item_rarity", IntegerParticleOptions::codec, IntegerParticleOptions::streamCodec);
    public static final Supplier<ParticleType<PositionParticleOptions>> BEACON = register("beacon", PositionParticleOptions::codec, PositionParticleOptions::streamCodec);
    public static final Supplier<SimpleParticleType> COMPOST = register("compost");
    public static final Supplier<SimpleParticleType> STEAM = register("steam");
    public static final Supplier<SimpleParticleType> END_PORTAL = register("end_portal");
    public static final Supplier<SimpleParticleType> END_CRYSTAL = register("end_crystal");
    public static final Supplier<SimpleParticleType> SCULK_DUST = register("sculk_dust");
    public static final Supplier<ParticleType<FloatParticleOptions>> SLIME_TRAIL = register("slime_trail", FloatParticleOptions::codec, FloatParticleOptions::streamCodec);
    public static final Supplier<ParticleType<FloatParticleOptions>> MAGMA_CUBE_TRAIL = register("magma_cube_trail", FloatParticleOptions::codec, FloatParticleOptions::streamCodec);
    public static final Supplier<ParticleType<ColorParticleOption>> SPELL_CASTER_MAGIC = register("spell_caster_magic", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<SimpleParticleType> AMETHYST_SPARKLE = register("amethyst_sparkle");
    public static final Supplier<SimpleParticleType> AZALEA_PETAL = register("azalea_petal");
    public static final Supplier<SimpleParticleType> FROSTY_BREATH = register("frosty_breath");
    public static final Supplier<ParticleType<FloatParticleOptions>> EXPERIENCE = register("experience", FloatParticleOptions::codec, FloatParticleOptions::streamCodec);
    public static final Supplier<SimpleParticleType> HEART_POP = register("heart_pop");
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_RING = register("potion_ring", ColorAndIntegerParticleOptions::codec, ColorAndIntegerParticleOptions::streamCodec);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_DOT = register("potion_dot", ColorAndIntegerParticleOptions::codec, ColorAndIntegerParticleOptions::streamCodec);
    public static final Supplier<ParticleType<ColorAndIntegerParticleOptions>> POTION_EMITTER = register("potion_emitter", ColorAndIntegerParticleOptions::codec, ColorAndIntegerParticleOptions::streamCodec);
    public static final Supplier<SimpleParticleType> IRON_GOLEM = register("iron_golem");
    public static final Supplier<SimpleParticleType> DROWNING_BUBBLE = register("drowning_bubble");
    public static final Supplier<SimpleParticleType> DROWNING_BUBBLE_POP = register("drowning_bubble_pop");
    public static final Supplier<ParticleType<DirectionParticleOptions>> EGG_SPLAT = register("egg_splat", DirectionParticleOptions::codec, DirectionParticleOptions::streamCodec);
    public static final Supplier<ParticleType<ColorParticleOption>> ENDER_EYE_PLACED_RING = register("ender_eye_placed_ring", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<ParticleType<BlockParticleOption>> BLOCK_NO_MOMENTUM = register("block_no_momentum", BlockParticleOption::codec, BlockParticleOption::streamCodec);
    public static final Supplier<SimpleParticleType> OMINOUS_VAULT_CONNECTION = register("ominous_vault_connection");
    public static final Supplier<ParticleType<GeyserSpoutParticleOptions>> GEYSER_SPOUT = register("geyser_spout", type -> GeyserSpoutParticleOptions.CODEC, type -> GeyserSpoutParticleOptions.STREAM_CODEC);
    public static final Supplier<SimpleParticleType> SNEEZE = register("sneeze");
    public static final Supplier<SimpleParticleType> GEYSER_SMOKE = register("geyser_smoke");
    public static final Supplier<ParticleType<ColorParticleOption>> POTION_CLOUD = register("potion_cloud", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<ParticleType<ColorParticleOption>> POTION_POOF_CLOUD = register("potion_poof_cloud", ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final Supplier<ParticleType<RippleParticleOptions>> RIPPLE = register("ripple", RippleParticleOptions::codec, RippleParticleOptions::streamCodec);
    public static final Supplier<ParticleType<DropletParticleOptions>> DROPLET = register("droplet", type -> DropletParticleOptions.CODEC, type -> DropletParticleOptions.STREAM_CODEC);
    public static final Supplier<ParticleType<SplashParticleOptions>> SPLASH = register("splash", type -> SplashParticleOptions.CODEC, type -> SplashParticleOptions.STREAM_CODEC);
    public static final Supplier<ParticleType<RippleParticleOptions>> SPLASH_RIPPLE = register("splash_ripple", RippleParticleOptions::codec, RippleParticleOptions::streamCodec);
    public static final Supplier<ParticleType<SplashEmitterParticleOptions>> SPLASH_EMITTER = register("splash_emitter", type -> SplashEmitterParticleOptions.CODEC, type -> SplashEmitterParticleOptions.STREAM_CODEC);
    public static final Supplier<SimpleParticleType> WATERFALL_CLOUD = register("waterfall_cloud");
    public static final Supplier<SimpleParticleType> WATERFALL_DROPLET = register("waterfall_droplet");
    public static final Supplier<SimpleParticleType> WATERFALL_MIST = register("waterfall_mist");
    public static final Supplier<ParticleType<FallenLeafParticleOptions>> FALLEN_LEAF = register("fallen_leaf", type -> FallenLeafParticleOptions.CODEC, type -> FallenLeafParticleOptions.STREAM_CODEC);
    public static final Supplier<SimpleParticleType> ARMADILLO = register("armadillo");
    public static final Supplier<SimpleParticleType> PURPLE_FLAME = register("purple_flame");
    public static final Supplier<SimpleParticleType> SKELETON_BONE = register("skeleton_bone");
    public static final Supplier<SimpleParticleType> WITHER_BONE = register("wither_bone");
    public static final Supplier<SimpleParticleType> STRAY_BONE = register("stray_bone");
    public static final Supplier<SimpleParticleType> BOGGED_BONE = register("bogged_bone");
    public static final Supplier<SimpleParticleType> CHARGED_ELECTRICITY = register("charged_electricity");
    public static final Supplier<SimpleParticleType> ELECTRICITY = register("electricity");

    public static void init() {
    }

    private static Supplier<SimpleParticleType> register(String name) {
        return REGISTRY.registerParticle(name, () -> new SimpleParticleType(false));
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParticleType<V>, V extends ParticleOptions> Supplier<T> register(String name, Function<ParticleType<V>, MapCodec<V>> codec, Function<ParticleType<V>, StreamCodec<? super RegistryFriendlyByteBuf, V>> streamCodec) {
        return (Supplier<T>) REGISTRY.registerParticle(name, () -> new ParticleType<V>(false) {

            @Override
            public MapCodec<V> codec() {
                return codec.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, V> streamCodec() {
                return streamCodec.apply(this);
            }
        });
    }
}

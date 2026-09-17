package einstein.subtle_effects.init;

import com.mojang.serialization.Codec;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.particle.option.*;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.util.platform.Registrar;
import me.fzzyhmstrs.fzzy_config.util.platform.RegistrySupplier;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Function;

public class ModParticles {

    private static final Registrar<ParticleType<?>> PARTICLE_TYPES = ConfigApiJava.platform().createRegistrar(SubtleEffects.MOD_ID, BuiltInRegistries.PARTICLE_TYPE);

    public static final RegistrySupplier<SimpleParticleType> SNORING = register("snoring");
    public static final RegistrySupplier<SimpleParticleType> FALLING_SNORING = register("falling_snoring");
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> LONG_SPARK = register("long_spark", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> SHORT_SPARK = register("short_spark", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> FLOATING_SPARK = register("floating_spark", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> METAL_SPARK = register("metal_spark", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> SNOW = register("snow");
    public static final RegistrySupplier<SimpleParticleType> SNOWBALL_TRAIL = register("snowball_trail");
    public static final RegistrySupplier<SimpleParticleType> FREEZING = register("freezing");
    public static final RegistrySupplier<SimpleParticleType> CHICKEN_FEATHER = register("chicken_feather");
    public static final RegistrySupplier<SimpleParticleType> BLUE_PARROT_FEATHER = register("blue_parrot_feather");
    public static final RegistrySupplier<SimpleParticleType> GRAY_PARROT_FEATHER = register("gray_parrot_feather");
    public static final RegistrySupplier<SimpleParticleType> GREEN_PARROT_FEATHER = register("green_parrot_feather");
    public static final RegistrySupplier<SimpleParticleType> RED_BLUE_PARROT_FEATHER = register("red_blue_parrot_feather");
    public static final RegistrySupplier<SimpleParticleType> YELLOW_BLUE_PARROT_FEATHER = register("yellow_blue_parrot_feather");
    public static final RegistrySupplier<SimpleParticleType> ALLAY_MAGIC = register("allay_magic");
    public static final RegistrySupplier<ParticleType<BooleanParticleOptions>> VEX_MAGIC = register("vex_magic", BooleanParticleOptions::codec, BooleanParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> SMALL_DUST_CLOUD = register("small_dust_cloud");
    public static final RegistrySupplier<SimpleParticleType> LARGE_DUST_CLOUD = register("large_dust_cloud");
    public static final RegistrySupplier<ParticleType<SheepFluffParticleOptions>> SHEEP_FLUFF = register("sheep_fluff", type -> SheepFluffParticleOptions.CODEC, SheepFluffParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> MUSHROOM_SPORE = register("mushroom_spore");
    public static final RegistrySupplier<SimpleParticleType> FIREFLY = register("firefly");
    public static final RegistrySupplier<SimpleParticleType> VANILLA_FIREFLY = register("vanilla_firefly");
    public static final RegistrySupplier<SimpleParticleType> FIREFLY_EMITTER = register("firefly_emitter");
    public static final RegistrySupplier<SimpleParticleType> SMOKE = register("smoke");
    public static final RegistrySupplier<SimpleParticleType> POLLEN = register("pollen");
    public static final RegistrySupplier<ParticleType<DirectionParticleOptions>> COMMAND_BLOCK = register("command_block", DirectionParticleOptions::codec, DirectionParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> ITEM_RARITY = register("item_rarity", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<PositionParticleOptions>> BEACON = register("beacon", PositionParticleOptions::codec, PositionParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> COMPOST = register("compost");
    public static final RegistrySupplier<SimpleParticleType> STEAM = register("steam");
    public static final RegistrySupplier<SimpleParticleType> END_PORTAL = register("end_portal");
    public static final RegistrySupplier<SimpleParticleType> END_CRYSTAL_MAGIC = register("end_crystal_magic");
    public static final RegistrySupplier<SimpleParticleType> SCULK_DUST = register("sculk_dust");
    public static final RegistrySupplier<ParticleType<FloatParticleOptions>> SLIME_TRAIL = register("slime_trail", FloatParticleOptions::codec, FloatParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<FloatParticleOptions>> MAGMA_CUBE_TRAIL = register("magma_cube_trail", FloatParticleOptions::codec, FloatParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> SPELL_CASTER_MAGIC = register("spell_caster_magic");
    public static final RegistrySupplier<SimpleParticleType> AMETHYST_SPARKLE = register("amethyst_sparkle");
    public static final RegistrySupplier<SimpleParticleType> AZALEA_PETAL = register("azalea_petal");
    public static final RegistrySupplier<SimpleParticleType> FROSTY_BREATH = register("frosty_breath");
    public static final RegistrySupplier<ParticleType<FloatParticleOptions>> EXPERIENCE = register("experience", FloatParticleOptions::codec, FloatParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> HEART_POP = register("heart_pop");
    public static final RegistrySupplier<ParticleType<PotionRingParticleOptions>> POTION_RING = register("potion_ring", PotionRingParticleOptions::codec, PotionRingParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<PotionRingParticleOptions>> POTION_DOT = register("potion_dot", PotionRingParticleOptions::codec, PotionRingParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<PotionRingParticleOptions>> POTION_EMITTER = register("potion_emitter", PotionRingParticleOptions::codec, PotionRingParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> IRON_GOLEM = register("iron_golem");
    public static final RegistrySupplier<SimpleParticleType> DROWNING_BUBBLE = register("drowning_bubble");
    public static final RegistrySupplier<SimpleParticleType> DROWNING_BUBBLE_POP = register("drowning_bubble_pop");
    public static final RegistrySupplier<ParticleType<ProjectileSplatParticleOptions>> EGG_SPLAT = register("egg_splat", ProjectileSplatParticleOptions::codec, ProjectileSplatParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> ENDER_EYE_PLACED_RING = register("ender_eye_placed_ring", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<BlockParticleOption>> BLOCK_NO_MOMENTUM = register("block_no_momentum", BlockParticleOption::codec, BlockParticleOption.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<GeyserSpoutParticleOptions>> GEYSER_SPOUT = register("geyser_spout", type -> GeyserSpoutParticleOptions.CODEC, GeyserSpoutParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> SNEEZE = register("sneeze");
    public static final RegistrySupplier<SimpleParticleType> GEYSER_SMOKE = register("geyser_smoke");
    public static final RegistrySupplier<ParticleType<ColorProviderParticleOptions>> POTION_CLOUD = register("potion_cloud", ColorProviderParticleOptions::codec, ColorProviderParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<ColorParticleOptions>> POTION_POOF_CLOUD = register("potion_poof_cloud", ColorParticleOptions::codec, ColorParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<RippleParticleOptions>> RIPPLE = register("ripple", RippleParticleOptions::codec, RippleParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<DropletParticleOptions>> DROPLET = register("droplet", type -> DropletParticleOptions.CODEC, DropletParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<SplashParticleOptions>> SPLASH = register("splash", type -> SplashParticleOptions.codec(), SplashParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<RippleParticleOptions>> SPLASH_RIPPLE = register("splash_ripple", RippleParticleOptions::codec, RippleParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<ParticleType<SplashEmitterParticleOptions>> SPLASH_EMITTER = register("splash_emitter", type -> SplashEmitterParticleOptions.codec(), SplashEmitterParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> WATERFALL_CLOUD = register("waterfall_cloud");
    public static final RegistrySupplier<SimpleParticleType> WATERFALL_DROPLET = register("waterfall_droplet");
    public static final RegistrySupplier<SimpleParticleType> WATERFALL_MIST = register("waterfall_mist");
    public static final RegistrySupplier<ParticleType<FallenLeafParticleOptions>> FALLEN_LEAF = register("fallen_leaf", type -> FallenLeafParticleOptions.CODEC, FallenLeafParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> ARMADILLO = register("armadillo");
    public static final RegistrySupplier<SimpleParticleType> PURPLE_FLAME = register("purple_flame");
    public static final RegistrySupplier<SimpleParticleType> SKELETON_BONE = register("skeleton_bone");
    public static final RegistrySupplier<SimpleParticleType> WITHER_BONE = register("wither_bone");
    public static final RegistrySupplier<SimpleParticleType> STRAY_BONE = register("stray_bone");
    public static final RegistrySupplier<SimpleParticleType> BOGGED_BONE = register("bogged_bone");
    public static final RegistrySupplier<SimpleParticleType> CHARGED_ELECTRICITY = register("charged_electricity");
    public static final RegistrySupplier<SimpleParticleType> ELECTRICITY = register("electricity");
    public static final RegistrySupplier<SimpleParticleType> ENCHANT_MAGIC = register("enchant_magic");
    public static final RegistrySupplier<SimpleParticleType> RISING_ENCHANT_GLYPHS = register("rising_enchant_glyphs");
    public static final RegistrySupplier<ParticleType<ProjectileSplatParticleOptions>> SNOWBALL_SPLAT = register("snow_splat", ProjectileSplatParticleOptions::codec, ProjectileSplatParticleOptions.DESERIALIZER);
    public static final RegistrySupplier<SimpleParticleType> HEART_GROWTH = register("heart_growth");

    public static void init() {
        PARTICLE_TYPES.init();
    }

    @SuppressWarnings("unchecked")
    private static RegistrySupplier<SimpleParticleType> register(String name) {
        return (RegistrySupplier<SimpleParticleType>) (Object) PARTICLE_TYPES.register(name, () -> new SimpleParticleType(false));
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private static <T extends ParticleType<V>, V extends ParticleOptions> RegistrySupplier<T> register(String name, Function<ParticleType<V>, Codec<V>> codec, ParticleOptions.Deserializer<V> deserializer) {
        return (RegistrySupplier<T>) PARTICLE_TYPES.register(name, () -> new ParticleType<>(false, deserializer) {

            @Override
            public Codec<V> codec() {
                return codec.apply(this);
            }
        });
    }
}

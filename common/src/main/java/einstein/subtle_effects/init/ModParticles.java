package einstein.subtle_effects.init;

import com.mojang.serialization.Codec;
import einstein.subtle_effects.particle.*;
import einstein.subtle_effects.particle.option.*;
import einstein.subtle_effects.particle.provider.*;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = register("snoring");
    public static final Supplier<SimpleParticleType> FALLING_SNORING = register("falling_snoring");
    public static final Supplier<SimpleParticleType> LONG_SPARK = register("long_spark");
    public static final Supplier<SimpleParticleType> LONG_SOUL_SPARK = register("long_soul_spark");
    public static final Supplier<SimpleParticleType> SHORT_SPARK = register("short_spark");
    public static final Supplier<SimpleParticleType> SHORT_SOUL_SPARK = register("short_soul_spark");
    public static final Supplier<SimpleParticleType> FLOATING_SPARK = register("floating_spark");
    public static final Supplier<SimpleParticleType> FLOATING_SOUL_SPARK = register("floating_soul_spark");
    public static final Supplier<SimpleParticleType> METAL_SPARK = register("metal_spark");
    public static final Supplier<SimpleParticleType> SNOW = register("snow");
    public static final Supplier<SimpleParticleType> SNOWBALL_TRAIL = register("snowball_trail");
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
    public static final Supplier<ParticleType<ColorParticleOptions>> SHEEP_FLUFF = register("sheep_fluff", ColorParticleOptions.DESERIALIZER, ColorParticleOptions::codec);
    public static final Supplier<SimpleParticleType> MUSHROOM_SPORE = register("mushroom_spore");
    public static final Supplier<SimpleParticleType> FIREFLY = register("firefly");
    public static final Supplier<SimpleParticleType> SMOKE = register("smoke");
    public static final Supplier<SimpleParticleType> POLLEN = register("pollen");
    public static final Supplier<ParticleType<DirectionParticleOptions>> COMMAND_BLOCK = register("command_block", DirectionParticleOptions.DESERIALIZER, DirectionParticleOptions::codec);
    public static final Supplier<ParticleType<ItemParticleOption>> ITEM_RARITY = register("item_rarity", ItemParticleOption.DESERIALIZER, ItemParticleOption::codec);
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

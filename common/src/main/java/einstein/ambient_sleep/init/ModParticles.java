package einstein.ambient_sleep.init;

import com.mojang.serialization.Codec;
import einstein.ambient_sleep.particle.*;
import einstein.ambient_sleep.particle.option.BooleanParticleOptions;
import einstein.ambient_sleep.particle.option.CommandBlockParticleOptions;
import einstein.ambient_sleep.particle.option.PositionParticleOptions;
import einstein.ambient_sleep.particle.option.SheepFluffParticleOptions;
import einstein.ambient_sleep.particle.provider.MushroomSporeProvider;
import einstein.ambient_sleep.particle.provider.PollenProvider;
import einstein.ambient_sleep.particle.provider.SmokeParticleProvider;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.ambient_sleep.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = register("snoring", SnoringParticle.Provider::new);
    public static final Supplier<SimpleParticleType> FALLING_SNORING = register("falling_snoring", SnoringParticle.FallingProvider::new);
    public static final Supplier<SimpleParticleType> LONG_SPARK = register("long_spark", SparkParticle.LongLifeProvider::new);
    public static final Supplier<SimpleParticleType> LONG_SOUL_SPARK = register("long_soul_spark", SparkParticle.LongLifeProvider::new);
    public static final Supplier<SimpleParticleType> SHORT_SPARK = register("short_spark", SparkParticle.ShortLifeProvider::new);
    public static final Supplier<SimpleParticleType> SHORT_SOUL_SPARK = register("short_soul_spark", SparkParticle.ShortLifeProvider::new);
    public static final Supplier<SimpleParticleType> FLOATING_SPARK = register("floating_spark", SparkParticle.FloatingProvider::new);
    public static final Supplier<SimpleParticleType> FLOATING_SOUL_SPARK = register("floating_soul_spark", SparkParticle.FloatingProvider::new);
    public static final Supplier<SimpleParticleType> METAL_SPARK = register("metal_spark", SparkParticle.MetalProvider::new);
    public static final Supplier<SimpleParticleType> SNOW = register("snow", SnowParticle.Provider::new);
    public static final Supplier<SimpleParticleType> SNOWBALL_TRAIL = register("snowball_trail", SnowParticle.SnowballTrailProvider::new);
    public static final Supplier<SimpleParticleType> CHICKEN_FEATHER = register("chicken_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> BLUE_PARROT_FEATHER = register("blue_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> GRAY_PARROT_FEATHER = register("gray_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> GREEN_PARROT_FEATHER = register("green_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> RED_BLUE_PARROT_FEATHER = register("red_blue_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> YELLOW_BLUE_PARROT_FEATHER = register("yellow_blue_parrot_feather", FeatherParticle.Provider::new);
    public static final Supplier<SimpleParticleType> ALLAY_MAGIC = register("allay_magic", AllayMagicParticle.Provider::new);
    public static final Supplier<ParticleType<BooleanParticleOptions>> VEX_MAGIC = register("vex_magic", BooleanParticleOptions.DESERIALIZER, BooleanParticleOptions::codec, AllayMagicParticle.VexProvider::new);
    public static final Supplier<SimpleParticleType> SMALL_DUST_CLOUD = register("small_dust_cloud", DustCloudParticle.SmallProvider::new);
    public static final Supplier<SimpleParticleType> LARGE_DUST_CLOUD = register("large_dust_cloud", DustCloudParticle.LargeProvider::new);
    public static final Supplier<ParticleType<SheepFluffParticleOptions>> SHEEP_FLUFF = register("sheep_fluff", SheepFluffParticleOptions.DESERIALIZER, type -> SheepFluffParticleOptions.CODEC, FeatherParticle.SheepFluffProvider::new);
    public static final Supplier<SimpleParticleType> MUSHROOM_SPORE = register("mushroom_spore", MushroomSporeProvider::new);
    public static final Supplier<SimpleParticleType> FIREFLY = register("firefly", sprites -> new InsectParticle.Provider(() -> new ParticleAnimation(sprites, 16, 3), true));
    public static final Supplier<SimpleParticleType> SMOKE = register("smoke", SmokeParticleProvider::new);
    public static final Supplier<SimpleParticleType> POLLEN = register("pollen", PollenProvider::new);
    public static final Supplier<ParticleType<CommandBlockParticleOptions>> COMMAND_BLOCK = register("command_block", CommandBlockParticleOptions.DESERIALIZER, type -> CommandBlockParticleOptions.CODEC, CommandBlockParticle.Provider::new);
    public static final Supplier<ParticleType<ItemParticleOption>> ITEM_RARITY = register("item_rarity", ItemParticleOption.DESERIALIZER, ItemParticleOption::codec, ItemRarityParticle.Provider::new);
    public static final Supplier<ParticleType<PositionParticleOptions>> BEACON = register("beacon", PositionParticleOptions.DESERIALIZER, PositionParticleOptions::codec, BeaconParticle.Provider::new);
    public static final Supplier<SimpleParticleType> COMPOST = register("compost", CompostParticle.Provider::new);
    public static final Supplier<SimpleParticleType> STEAM = register("steam", SteamParticle.Provider::new);

    public static void init() {
    }

    private static Supplier<SimpleParticleType> register(String name, Function<SpriteSet, ParticleProvider<SimpleParticleType>> provider) {
        Supplier<SimpleParticleType> particleType = REGISTRY.registerParticle(name, () -> new SimpleParticleType(false));
        REGISTRY.registerParticleProvider(particleType, provider);
        return particleType;
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    private static <T extends ParticleType<V>, V extends ParticleOptions> Supplier<T> register(String name, ParticleOptions.Deserializer<V> deserializer, Function<ParticleType<V>, Codec<V>> codec, Function<SpriteSet, ParticleProvider<V>> provider) {
        Supplier<T> particleType = (Supplier<T>) REGISTRY.registerParticle(name, () -> new ParticleType<V>(false, deserializer) {

            @Override
            public Codec<V> codec() {
                return codec.apply(this);
            }
        });
        REGISTRY.registerParticleProvider(particleType, provider);
        return particleType;
    }
}

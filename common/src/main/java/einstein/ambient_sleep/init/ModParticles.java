package einstein.ambient_sleep.init;

import einstein.ambient_sleep.client.particle.FeatherParticle;
import einstein.ambient_sleep.client.particle.SnoringParticle;
import einstein.ambient_sleep.client.particle.SnowballTrailParticle;
import einstein.ambient_sleep.client.particle.SparkParticle;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

import static einstein.ambient_sleep.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = REGISTRY.registerParticle("snoring", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> LONG_SPARK = REGISTRY.registerParticle("long_spark", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> LONG_SOUL_SPARK = REGISTRY.registerParticle("long_soul_spark", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SHORT_SPARK = REGISTRY.registerParticle("short_spark", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SHORT_SOUL_SPARK = REGISTRY.registerParticle("short_soul_spark", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SNOWBALL_TRAIL = REGISTRY.registerParticle("snowball_trail", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> CHICKEN_FEATHER = REGISTRY.registerParticle("chicken_feather", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BLUE_PARROT_FEATHER = REGISTRY.registerParticle("blue_parrot_feather", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> GRAY_PARROT_FEATHER = REGISTRY.registerParticle("gray_parrot_feather", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> GREEN_PARROT_FEATHER = REGISTRY.registerParticle("green_parrot_feather", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RED_BLUE_PARROT_FEATHER = REGISTRY.registerParticle("red_blue_parrot_feather", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> YELLOW_BLUE_PARROT_FEATHER = REGISTRY.registerParticle("yellow_blue_parrot_feather", () -> new SimpleParticleType(false));

    public static void init() {
        REGISTRY.registerParticleProvider(SNORING, SnoringParticle.Provider::new);
        REGISTRY.registerParticleProvider(LONG_SPARK, SparkParticle.LongLifeProvider::new);
        REGISTRY.registerParticleProvider(LONG_SOUL_SPARK, SparkParticle.LongLifeProvider::new);
        REGISTRY.registerParticleProvider(SHORT_SPARK, SparkParticle.ShortLifeProvider::new);
        REGISTRY.registerParticleProvider(SHORT_SOUL_SPARK, SparkParticle.ShortLifeProvider::new);
        REGISTRY.registerParticleProvider(SNOWBALL_TRAIL, SnowballTrailParticle.Provider::new);
        REGISTRY.registerParticleProvider(CHICKEN_FEATHER, FeatherParticle.Provider::new);
        REGISTRY.registerParticleProvider(BLUE_PARROT_FEATHER, FeatherParticle.Provider::new);
        REGISTRY.registerParticleProvider(GRAY_PARROT_FEATHER, FeatherParticle.Provider::new);
        REGISTRY.registerParticleProvider(GREEN_PARROT_FEATHER, FeatherParticle.Provider::new);
        REGISTRY.registerParticleProvider(RED_BLUE_PARROT_FEATHER, FeatherParticle.Provider::new);
        REGISTRY.registerParticleProvider(YELLOW_BLUE_PARROT_FEATHER, FeatherParticle.Provider::new);
    }
}

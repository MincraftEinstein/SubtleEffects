package einstein.ambient_sleep.init;

import einstein.ambient_sleep.client.particle.ChickenFeatherParticle;
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

    public static void init() {
        REGISTRY.registerParticleProvider(SNORING, SnoringParticle.Provider::new);
        REGISTRY.registerParticleProvider(LONG_SPARK, SparkParticle.LongLifeProvider::new);
        REGISTRY.registerParticleProvider(LONG_SOUL_SPARK, SparkParticle.LongLifeProvider::new);
        REGISTRY.registerParticleProvider(SHORT_SPARK, SparkParticle.ShortLifeProvider::new);
        REGISTRY.registerParticleProvider(SHORT_SOUL_SPARK, SparkParticle.ShortLifeProvider::new);
        REGISTRY.registerParticleProvider(SNOWBALL_TRAIL, SnowballTrailParticle.Provider::new);
        REGISTRY.registerParticleProvider(CHICKEN_FEATHER, ChickenFeatherParticle.Provider::new);
    }
}

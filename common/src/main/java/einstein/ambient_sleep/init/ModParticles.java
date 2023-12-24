package einstein.ambient_sleep.init;

import einstein.ambient_sleep.client.particle.SnoringParticle;
import einstein.ambient_sleep.client.particle.SparkParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

import static einstein.ambient_sleep.platform.Services.REGISTRY;

public class ModParticles {

    public static final Supplier<SimpleParticleType> SNORING = REGISTRY.registerParticle("snoring", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SPARK = REGISTRY.registerParticle("spark", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SOUL_SPARK = REGISTRY.registerParticle("soul_spark", () -> new SimpleParticleType(false));

    public static void init() {
        REGISTRY.registerParticleProvider(SNORING, SnoringParticle.Provider::new);
        REGISTRY.registerParticleProvider(SPARK, SparkParticle.Provider::new);
        REGISTRY.registerParticleProvider(SOUL_SPARK, SparkParticle.Provider::new);
    }
}

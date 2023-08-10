package einstein.ambient_sleep.init;

import einstein.ambient_sleep.platform.Services;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModInit {

    public static final Supplier<SimpleParticleType> SNORING = Services.REGISTRY.registerParticle("snoring", () -> new SimpleParticleType(false));

    public static void init() {
    }
}

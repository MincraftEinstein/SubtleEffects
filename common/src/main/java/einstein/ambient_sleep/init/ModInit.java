package einstein.ambient_sleep.init;

import einstein.ambient_sleep.platform.Services;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModInit {

    public static final Supplier<SimpleParticleType> SNORING_PARTICLE = Services.REGISTRY.registerParticle("snoring", () -> new SimpleParticleType(false));
    public static final Supplier<SoundEvent> VILLAGER_SLEEP = Services.REGISTRY.registerSound("entity.villager.sleep");
    public static final Supplier<SoundEvent> PLAYER_SLEEP = Services.REGISTRY.registerSound("entity.player.sleep");

    public static void init() {
    }
}

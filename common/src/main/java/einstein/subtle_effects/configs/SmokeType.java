package einstein.subtle_effects.configs;

import einstein.subtle_effects.init.ModParticles;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;

public enum SmokeType implements EnumTranslatable {
    OFF(null),
    DEFAULT(() -> () -> ParticleTypes.SMOKE),
    UPDATED(() -> ModParticles.SMOKE);

    @Nullable
    private final Supplier<Supplier<? extends ParticleOptions>> particle;

    SmokeType(@Nullable Supplier<Supplier<? extends ParticleOptions>> particle) {
        this.particle = particle;
    }

    @NotNull
    @Override
    public String prefix() {
        return BASE_KEY + "smokeType";
    }

    public boolean isEnabled() {
        return this != OFF;
    }

    @Nullable
    public Supplier<? extends ParticleOptions> getParticle() {
        return particle.get();
    }
}

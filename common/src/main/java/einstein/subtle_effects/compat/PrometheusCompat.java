package einstein.subtle_effects.compat;

import einstein.subtle_effects.data.BurningEffects;
import einstein.subtle_effects.data.BurningEffectsReloadListener;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import it.crystalnest.prometheus.api.type.FireTyped;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import static einstein.subtle_effects.SubtleEffects.loc;

public class PrometheusCompat {

    @Nullable
    public static ParticleOptions getFlameParticle(Entity entity) {
        BurningEffects burningEffects = getBurningEffect(entity);
        if (burningEffects != null) {
            return burningEffects.flameParticle().orElseGet(() ->
                    FireManager.getComponent(getFireType(entity), Fire.Component.FLAME_PARTICLE)
            );
        }
        return null;
    }

    @Nullable
    public static ColorProviderType.ColorProvider getSparkColors(Entity entity) {
        BurningEffects burningEffects = getBurningEffect(entity);
        if (burningEffects != null) {
            return burningEffects.colorProvider();
        }
        return null;
    }

    @Nullable
    private static BurningEffects getBurningEffect(Entity entity) {
        return BurningEffectsReloadListener.PROMETHEUS_BURNING_EFFECTS.get(getFireType(entity));
    }

    private static Identifier getFireType(Entity entity) {
        return ((FireTyped) entity).getFireType();
    }
}

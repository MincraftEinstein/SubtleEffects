package einstein.subtle_effects.compat;

import einstein.subtle_effects.particle.SparkParticle;
import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import it.crystalnest.prometheus.api.type.FireTyped;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static einstein.subtle_effects.SubtleEffects.loc;

public class PrometheusCompat {
    // TODO fix when update

    public static final Identifier SOUL_FIRE_ID = Identifier.withDefaultNamespace("soul");

    @Nullable
    public static ParticleOptions getFlameParticle(Entity entity) {
        return null;//FireManager.getComponent(getFireType(entity), Fire.Component.FLAME_PARTICLE);
    }

    @Nullable
    public static List<Integer> getSparkParticle(Entity entity) {
        Identifier fireType = loc("bad");// getFireType(entity);
        ParticleOptions flameParticle = getFlameParticle(entity);

        if (/*fireType.equals(FireManager.DEFAULT_FIRE_TYPE) || */ParticleTypes.FLAME.equals(flameParticle)) {
            return SparkParticle.DEFAULT_COLORS;
        }
        else if (fireType.equals(SOUL_FIRE_ID) || ParticleTypes.SOUL_FIRE_FLAME.equals(flameParticle)) {
            return SparkParticle.SOUL_COLORS;
        }
        return null;
    }

//    private static Identifier getFireType(Entity entity) {
//        return ((FireTyped) entity).getFireType();
//    }
}

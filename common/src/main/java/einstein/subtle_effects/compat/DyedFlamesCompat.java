package einstein.subtle_effects.compat;

import einstein.subtle_effects.data.BurningEffects;
import einstein.subtle_effects.data.BurningEffectsReloadListener;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DyedFlamesCompat {

    public static ParticleOptions getFlameParticle(Entity entity) {
//        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.get(entity);
        Block block = null;
        if (block != null) {
            BurningEffects burningEffects = getBurningEffects(block);
            if (burningEffects != null) {
                Optional<SimpleParticleType> particleType = burningEffects.flameParticle();
                if (particleType.isPresent()) {
                    return particleType.get();
                }
            }

//            Optional<FireType> fireType = FireType.getFireType(block);
//            if (fireType.isPresent()) {
//                return fireType.get().particleType().orElse(null);
//            }
        }
        return null;
    }

    @Nullable
    public static ColorProviderType.ColorProvider getSparkColors(Entity entity) {
//        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.get(entity);
        Block block = null;
        if (block != null) {
            BurningEffects burningEffects = getBurningEffects(block);
            if (burningEffects != null) {
                return burningEffects.colorProvider();
            }
        }
        return null;
    }

    @Nullable
    private static BurningEffects getBurningEffects(Block block) {
        return BurningEffectsReloadListener.DYED_FLAMES_BURNING_EFFECTS.get(BuiltInRegistries.BLOCK.getKey(block));
    }
}

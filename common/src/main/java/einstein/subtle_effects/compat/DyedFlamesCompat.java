package einstein.subtle_effects.compat;

import einstein.subtle_effects.particle.SparkParticle;
import fuzs.dyedflames.init.ModRegistry;
import fuzs.dyedflames.world.level.block.FireType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DyedFlamesCompat {

    public static ParticleOptions getFlameParticle(Entity entity) {
        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.get(entity);
        if (block != null) {
            if (block == Blocks.FIRE || block == Blocks.LAVA) {
                return ParticleTypes.FLAME;
            }
            else if (block == Blocks.SOUL_FIRE) {
                return ParticleTypes.SOUL_FIRE_FLAME;
            }

            Optional<FireType> fireType = FireType.getFireType(block);
            if (fireType.isPresent()) {
                return fireType.get().particleType().orElse(null);
            }
        }
        return null;
    }

    @Nullable
    public static List<Integer> getSparkParticle(Entity entity) {
        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.get(entity);
        if (block == Blocks.FIRE) {
            return SparkParticle.DEFAULT_COLORS;
        }
        else if (block == Blocks.SOUL_FIRE) {
            return SparkParticle.SOUL_COLORS;
        }
        return null;
    }
}

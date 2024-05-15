package einstein.ambient_sleep.util;

import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.nextSign;

public class ParticleManager {

    public static void entityFell(LivingEntity entity, double y, float distance, int fallDamage) {
        if (entity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            return;
        }

        if (!INSTANCE.fallDamageDustClouds.get()) {
            return;
        }

        if (fallDamage <= 0 && !((entity instanceof AbstractHorse) && distance > (entity instanceof Camel ? 0.5 : 1))) {
            return;
        }

        if (entity.isInWater() || entity.isInLava() || entity.isInPowderSnow) {
            return;
        }

        Level level = entity.level();
        RandomSource random = entity.getRandom();

        if (entity instanceof Strider strider) {
            if (level.getFluidState(strider.getOnPos().atY(Mth.floor(y))).is(FluidTags.LAVA)) {
                return;
            }
        }

        if (fallDamage < 4) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ModParticles.SMALL_DUST_CLOUD.get(),
                        entity.getRandomX(1),
                        y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                        entity.getRandomZ(1),
                        0.3 * nextSign(),
                        random.nextDouble(),
                        0.3 * nextSign()
                );
            }
            return;
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                    entity.getRandomX(1),
                    y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                    entity.getRandomZ(1),
                    0.5 * nextSign(),
                    random.nextDouble() * 3,
                    0.5 * nextSign()
            );
        }
    }

    @FunctionalInterface
    public interface EntityProvider<T extends Entity> {

        void apply(T entity, Level level, RandomSource random);
    }

    @FunctionalInterface
    public interface BlockProvider {

        void apply(BlockState state, Level level, BlockPos pos, RandomSource random);
    }
}

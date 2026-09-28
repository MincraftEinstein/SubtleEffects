package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SlimeTrailTicker<T extends Slime> extends EntityTicker<T> {

    private final boolean isMagma;
    private final Supplier<ParticleType<FloatParticleOptions>> type;
    private boolean wasInAir;

    public SlimeTrailTicker(T entity, Supplier<ParticleType<FloatParticleOptions>> type) {
        super(entity);
        this.type = type;
        isMagma = entity.getType().equals(EntityType.MAGMA_CUBE);
    }

    @Override
    public void entityTick() {
        if (wasInAir && entity.onGround()) {
            int size = entity.getSize();
            BlockPos pos = entity.blockPosition();
            BlockState state = level.getBlockState(pos);
            double height = getCollisionHeight(state, pos);

            if (height <= 0) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                height = getCollisionHeight(belowState, belowPos);
                pos = belowPos;

                // This is in case the 'below block' also doesn't have a block below it,
                // if this happens then a trail particle probably shouldn't be spawned
                if (height <= 0) {
                    height = Double.NaN;
                }
            }

            if (!Double.isNaN(height)) {
                level.addParticle(new FloatParticleOptions(type.get(), size * 0.5F),
                        entity.getX(),
                        pos.getY() + height + (random.nextDouble() / 10),
                        entity.getZ(),
                        0, 0, 0
                );
            }

            if (isMagma && ModConfigs.ENTITIES.magmaCubeLandSparks) {
                for (int i = 0; i < 20 * size; i++) {
                    level.addParticle(SparkParticle.create(SparkType.LONG_LIFE, random),
                            entity.getRandomX(0.9),
                            entity.getY(),
                            entity.getRandomZ(0.9),
                            0, 0, 0
                    );
                }
            }
        }
        wasInAir = !entity.onGround();
    }

    private double getCollisionHeight(BlockState state, BlockPos pos) {
        return state.isAir() ? 0 : state.getCollisionShape(level, pos).max(Direction.Axis.Y);
    }
}

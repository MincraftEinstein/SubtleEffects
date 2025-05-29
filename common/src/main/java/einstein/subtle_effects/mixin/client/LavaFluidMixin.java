package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.configs.blocks.SparksConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.SparkType;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.util.MathUtil.*;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

    @Shadow
    public abstract boolean isSame(Fluid fluid);

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        SparksConfigs.LavaSparksSpawnType type = ModConfigs.BLOCKS.sparks.lavaSparks;
        if (type.equals(SparksConfigs.LavaSparksSpawnType.OFF)
                || (type.equals(SparksConfigs.LavaSparksSpawnType.NOT_NETHER)
                && level.dimension().equals(Level.NETHER))) {
            return;
        }

        if (state.hasProperty(LavaFluid.FALLING) && state.getValue(LavaFluid.FALLING)) {
            for (Direction direction : Direction.values()) {
                BlockPos relativePos = pos.relative(direction);
                if (direction.getAxis() != Direction.Axis.Y && !Util.isSolidOrNotEmpty(level, relativePos)) {
                    ParticleSpawnUtil.spawnParticlesOnSide(SparkParticle.create(SparkType.FLOATING, random),
                            -0.0625F,
                            direction.getOpposite(),
                            level, relativePos, random,
                            MathUtil.nextNonAbsDouble(random, 0.1),
                            MathUtil.nextNonAbsDouble(random, 0.07),
                            MathUtil.nextNonAbsDouble(random, 0.1)
                    );
                }
            }
            return;
        }

        int count = 1;
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockPos relativePos = pos.offset(x, 0, z);

                if (!isSame(level.getFluidState(relativePos).getType()) || level.getBlockState(relativePos).isSolidRender()) {
                    count = 5;
                    continue;
                }

                BlockPos abovePos = relativePos.above();
                if (Util.isSolidOrNotEmpty(level, abovePos)) {
                    if (abovePos.equals(pos.above())) {
                        return;
                    }

                    count = 5;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            level.addParticle(SparkParticle.create(SparkType.FLOATING, random),
                    pos.getX() + 0.5 + random.nextDouble() / 2 * nextSign(random),
                    pos.getY() + 0.75 + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + 0.5 + random.nextDouble() / 2 * nextSign(random),
                    nextNonAbsDouble(random, 0.1),
                    nextDouble(random, 0.07),
                    nextNonAbsDouble(random, 0.1)
            );
        }
    }
}

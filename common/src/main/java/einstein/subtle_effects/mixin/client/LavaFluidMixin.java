package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.ParticleSpawnUtil;
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

import static einstein.subtle_effects.init.ModConfigs.INSTANCE;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

    @Shadow
    public abstract boolean isSame(Fluid fluid);

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        if (INSTANCE.lavaSparks.get().equals(ModConfigs.LavaSparksSpawnType.OFF)
                || (INSTANCE.lavaSparks.get().equals(ModConfigs.LavaSparksSpawnType.NOT_NETHER)
                && level.dimension().equals(Level.NETHER))) {
            return;
        }

        if (state.getValue(LavaFluid.FALLING)) {
            for (Direction direction : Direction.values()) {
                BlockPos relativePos = pos.relative(direction);
                if (direction.getAxis() != Direction.Axis.Y && !Util.isSolidOrNotEmpty(level, relativePos)) {
                    ParticleSpawnUtil.spawnParticlesOnSide(ModParticles.FLOATING_SPARK.get(),
                            -0.0625F,
                            direction.getOpposite(),
                            level, relativePos, random,
                            MathUtil.nextFloat(10) * MathUtil.nextSign(),
                            MathUtil.nextFloat(7) ,
                            MathUtil.nextFloat(10) * MathUtil.nextSign()
                    );
                }
            }
            return;
        }

        int count = 5;
        int poolSize = 0;

        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockPos relativePos = pos.offset(x, 0, z);
                BlockPos abovePos = relativePos.above();

                if (level.getBlockState(relativePos).isSolidRender(level, relativePos) || !isSame(level.getFluidState(relativePos).getType())) {
                    continue;
                }

                if (Util.isSolidOrNotEmpty(level, abovePos)) {
                    if (abovePos.equals(pos.above())) {
                        return;
                    }
                    continue;
                }

                poolSize++;
            }
        }

        if (poolSize == 9) {
            count = 1;
        }

        ParticleSpawnUtil.spawnLavaSparks(level, pos, random, count);
    }
}

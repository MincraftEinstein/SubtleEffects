package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.core.BlockPos;
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

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

    @Shadow
    public abstract boolean isSame(Fluid fluid);

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        if (!state.isSource()) {
            return;
        }

        if (INSTANCE.lavaSparks.get().equals(ModConfigs.LavaSparksSpawnType.OFF)
                || (INSTANCE.lavaSparks.get().equals(ModConfigs.LavaSparksSpawnType.OVERWORLD_ONLY)
                && !level.dimension().equals(Level.OVERWORLD))) {
            return;
        }

        int count = 5;
        int poolSize = 0;

        for (int x = 0; x < 3; ++x) {
            for (int z = 0; z < 3; ++z) {
                if (isSame(level.getFluidState(pos.offset(x, 0, z)).getType())) {
                    poolSize++;
                }
            }
        }

        if (poolSize == 9) {
            count = 1;
        }

        for (int i = 0; i < count; i++) {
            level.addParticle(ModParticles.FLOATING_SPARK.get(),
                    pos.getX() + 0.5 + random.nextDouble() / 2 * (random.nextBoolean() ? 1 : -1),
                    pos.getY() + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + 0.5 + random.nextDouble() / 2 * (random.nextBoolean() ? 1 : -1),
                    random.nextInt(10) / 100D * (random.nextBoolean() ? 1 : -1),
                    random.nextInt(7) / 100D,
                    random.nextInt(10) / 100D * (random.nextBoolean() ? 1 : -1)
            );
        }
    }
}

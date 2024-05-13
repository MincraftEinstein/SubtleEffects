package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        ParticleSpawnUtil.spawnHeatedWaterParticles(level, pos, random, state.getValue(FALLING),
                state.getHeight(level, pos), ModConfigs.INSTANCE.steamingWater, ModConfigs.INSTANCE.boilingWater);
    }
}

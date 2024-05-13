package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.MathUtil;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
        int brightness = level.getBrightness(LightLayer.BLOCK, pos);
        if (brightness > 11 || level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK)) {
            if (!state.getValue(FALLING) && !Util.isSolidOrNotEmpty(level, pos.above())) {
                level.addParticle(ModParticles.STEAM.get(),
                        pos.getX() + random.nextDouble(),
                        pos.getY() + 0.875 + MathUtil.nextFloat(50),
                        pos.getZ() + random.nextDouble(),
                        0, 0, 0
                );
            }

            if (brightness >= 13) {
                level.addParticle(ParticleTypes.BUBBLE,
                        pos.getX() + random.nextDouble(),
                        Mth.clamp(random.nextDouble(), pos.getY(), pos.getY() + state.getHeight(level, pos)),
                        pos.getZ() + random.nextDouble(),
                        0, 0, 0
                );
            }
        }
    }
}

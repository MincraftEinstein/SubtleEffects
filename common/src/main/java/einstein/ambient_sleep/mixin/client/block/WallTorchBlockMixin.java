package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WallTorchBlock.class)
public class WallTorchBlockMixin {

    @Redirect(method = "animateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke() {
        if (ModConfigs.INSTANCE.torchSmoke.get()) {
            return ModParticles.SMOKE.get();
        }
        return ParticleTypes.SMOKE;
    }

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!ModConfigs.INSTANCE.torchSparks.get()) {
            return;
        }

        Direction direction = state.getValue(WallTorchBlock.FACING).getOpposite();
        Util.spawnSparks(level, random, pos, new Vec3(0.5 + (0.27 * direction.getStepX()), 0.7, 0.5 + (0.27 * direction.getStepZ())), new Vec3i(1, 1, 1), 2, 20, state.is(Blocks.SOUL_WALL_TORCH), false);
    }
}

package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCandleBlock.class)
public abstract class AbstractCandleBlockMixin {

    @Shadow
    protected abstract Iterable<Vec3> getParticleOffsets(BlockState state);

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (ModConfigs.INSTANCE.enableCandleSparks.get() && state.getValue(AbstractCandleBlock.LIT)) {
            getParticleOffsets(state).forEach(offset ->
                    Util.spawnSparks(level, random, pos, offset, new Vec3i(1, 1, 1), 1, 20, false, false));
        }
    }
}

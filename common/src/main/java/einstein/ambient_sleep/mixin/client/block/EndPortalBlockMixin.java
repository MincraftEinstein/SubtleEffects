package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (ModConfigs.INSTANCE.replaceEndPortalParticles.get()) {
            for (int i = 0; i < 3; ++i) {
                level.addParticle(ParticleTypes.PORTAL,
                        pos.getX() + random.nextDouble(),
                        pos.getY(),
                        pos.getZ() + random.nextDouble(),
                        (random.nextDouble() - 0.5) * 0.5,
                        random.nextDouble(),
                        (random.nextDouble() - 0.5) * 0.5
                );
            }

            ci.cancel();
        }
    }
}

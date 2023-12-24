package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.getValue(CampfireBlock.LIT)) {
            if (random.nextInt(1) == 0) {
                for (int i = 0; i < 10; i++) {
                    SimpleParticleType type = (state.is(Blocks.SOUL_CAMPFIRE) ? ModParticles.SOUL_SPARK : ModParticles.SPARK).get();
                    level.addParticle(type,
                            pos.getX() + 0.5 + random.nextDouble() / 6 * (random.nextBoolean() ? 1 : -1),
                            pos.getY() + 0.4,
                            pos.getZ() + 0.5 + random.nextDouble() / 6 * (random.nextBoolean() ? 1 : -1),
                            random.nextInt(3) / 100D, random.nextInt(5) / 100D, random.nextInt(3) / 100D
                    );
                }
            }
        }
    }
}

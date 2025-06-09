package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    @WrapWithCondition(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private boolean animateTick(Level level, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) RandomSource random) {
        if (random.nextBoolean() && ModConfigs.BLOCKS.endPortalParticles) {
            level.addParticle(ModParticles.END_PORTAL.get(),
                    pos.getX() + random.nextDouble(),
                    pos.getY() + random.nextInt(3) + random.nextDouble(),
                    pos.getZ() + random.nextDouble(),
                    0, 0, 0
            );
        }

        if (ModConfigs.BLOCKS.replaceEndPortalSmoke) {
            level.addParticle(ParticleTypes.PORTAL,
                    pos.getX() + random.nextDouble(),
                    pos.getY(),
                    pos.getZ() + random.nextDouble(),
                    (random.nextDouble() - 0.5) * 0.5,
                    random.nextDouble(),
                    (random.nextDouble() - 0.5) * 0.5
            );

            return false;
        }

        return true;
    }
}

package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.SplashEmitterParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private float radius;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    @Inject(method = "finalizeExplosion", at = @At("HEAD"))
    private void addSplashes(boolean spawnParticles, CallbackInfo ci) {
        if (spawnParticles && level.isClientSide && ModConfigs.ENTITIES.splashes.explosionsCauseSplashes) {
            BlockPos pos = BlockPos.containing(x, y, z);
            FluidState fluidState = level.getFluidState(pos);

            if (!fluidState.isEmpty()) {
                int blockY = pos.getY();

                for (int y = blockY; y < blockY + (radius * 1.5) + 1; y++) {
                    BlockPos currentPos = pos.atY(y);
                    FluidState currentFluidState = level.getFluidState(currentPos);

                    if (fluidState.getType().isSame(currentFluidState.getType())) {
                        continue;
                    }

                    if (level.getBlockState(currentPos).isSolidRender(level, currentPos)) {
                        return;
                    }

                    ParticleType<SplashEmitterParticleOptions> type = fluidState.is(FluidTags.WATER) ? ModParticles.WATER_SPLASH_EMITTER.get() : fluidState.is(FluidTags.LAVA) ? ModParticles.LAVA_SPLASH_EMITTER.get() : null;
                    if (type != null) {
                        BlockPos surfacePos = currentPos.below();
                        FluidState surfaceFluidState = level.getFluidState(surfacePos);
                        float scale = radius - ((y - blockY) / radius);

                        level.addAlwaysVisibleParticle(new SplashEmitterParticleOptions(type, scale, scale * (scale * 0.1F), -1, -1),
                                true, x, surfacePos.getY() + surfaceFluidState.getHeight(level, surfacePos) + 0.01, z,
                                0, 0, 0
                        );
                    }
                    return;
                }
            }
        }
    }
}

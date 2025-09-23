package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.SplashDropletParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.util.MathUtil.nextDouble;
import static einstein.subtle_effects.util.MathUtil.nextSign;

@Mixin(Wolf.class)
public class WolfMixin {

    @Unique
    private final Wolf subtleEffects$me = (Wolf) (Object) this;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void replaceSplashParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ENTITIES.improvedWetWolfShakeEffects && options.getType() == ParticleTypes.SPLASH && level.isClientSide) {
            RandomSource random = subtleEffects$me.getRandom();
            if (random.nextInt(2) != 0) {
                return;
            }

            int xSign = nextSign(random);
            int zSign = nextSign(random);
            float rotation = -subtleEffects$me.getPreciseBodyRotation(Util.getPartialTicks()) * Mth.DEG_TO_RAD;
            boolean isSitting = subtleEffects$me.isInSittingPose();
            boolean isBaby = subtleEffects$me.isBaby();

            Vec3 pos = new Vec3(nextDouble(random, 0.6) * xSign, 0, (nextDouble(random, 0.6) * zSign) + (isSitting ? 0.2 : -0.2))
                    .yRot(rotation)
                    .add(subtleEffects$me.getX(), 0, subtleEffects$me.getZ());

            Vec3 speed = new Vec3(nextDouble(random, 0.15) * xSign, 0, nextDouble(random, 0.15) * zSign)
                    .yRot(rotation);

            if (isSitting) {
                y -= 0.2F;
            }

            if (isBaby) {
                y -= 0.3F;
            }

            level.addParticle(new SplashDropletParticleOptions(ModParticles.WATER_SPLASH_DROPLET.get(), 0.75F, 1, true),
                    pos.x(), y, pos.z(), speed.x(), speed.y(), speed.z()
            );
            return;
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}

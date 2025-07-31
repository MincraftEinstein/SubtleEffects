package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloud.class)
public abstract class AreaEffectCloudMixin {

    @Unique
    private int subtleEffects$waitedTime;

    @Unique
    private final AreaEffectCloud subtleEffects$me = (AreaEffectCloud) (Object) this;

    @Shadow
    public abstract boolean isWaiting();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (subtleEffects$me.level().isClientSide()) {
            if (isWaiting() && subtleEffects$waitedTime < 10) {
                subtleEffects$waitedTime++;
            }
        }
    }

    @WrapOperation(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private void replaceWaitingParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            if (subtleEffects$waitedTime < 10) {
                return;
            }

            options = ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(), -1);
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private void replaceEffectParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            boolean isWaiting = isWaiting();
            if (isWaiting && subtleEffects$waitedTime < 10) {
                return;
            }

            RandomSource random = subtleEffects$me.getRandom();

            if (random.nextInt(isWaiting ? 4 : 20) == 0) {
                ColorParticleOption colorOption = (ColorParticleOption) options;
                if (isWaiting) {
                    level.addParticle(ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(),
                                    colorOption.getRed(), colorOption.getGreen(), colorOption.getBlue()),
                            x, y, z, 0, 0, 0
                    );
                }
                else {
                    subtleEffects$spawnPotionCloud(level, random, x, z,
                            colorOption.getRed(), colorOption.getGreen(), colorOption.getBlue()
                    );
                }
            }

            if (!isWaiting && random.nextInt(20) > 0) {
                return;
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 3))
    private void replaceDragonBreathParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ENTITIES.dragonsBreathClouds) {
            if (options.getType() == ParticleTypes.DRAGON_BREATH) {
                RandomSource random = subtleEffects$me.getRandom();

                if (random.nextInt(35) == 0) {
                    subtleEffects$spawnPotionCloud(level, random, x, z,
                            Mth.nextFloat(random, 0.7176471F, 0.8745098F),
                            0,
                            Mth.nextFloat(random, 0.8235294F, 0.9764706F)
                    );
                }

                if (random.nextInt(20) > 0) {
                    return;
                }
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Unique
    private void subtleEffects$spawnPotionCloud(Level level, RandomSource random, double x, double z, float red, float green, float blue) {
        level.addAlwaysVisibleParticle(
                ColorParticleOption.create(ModParticles.POTION_CLOUD.get(), red, green, blue),
                x, subtleEffects$me.getY() + (random.nextDouble() / 10), z, 0, 0, 0
        );
    }
}

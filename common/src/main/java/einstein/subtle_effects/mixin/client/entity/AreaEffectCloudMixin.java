package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow
    @Final
    private static EntityDataAccessor<ParticleOptions> DATA_PARTICLE;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (subtleEffects$me.level().isClientSide()) {
            if (isWaiting() && subtleEffects$waitedTime < 10) {
                subtleEffects$waitedTime++;
            }
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private void replaceWaitingParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            if (subtleEffects$waitedTime < 10) {
                return;
            }

            options = ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(), -1);
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private void replaceEffectParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            ColorParticleOption colorOptions = (ColorParticleOption) options;
            if (subtleEffects$spawnParticles(level, colorOptions.getRed(), colorOptions.getGreen(), colorOptions.getBlue(),
                    x, y, z, 20, 4)) {
                return;
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 2))
    private void replaceNonEffectWaitingParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            if (CompatHelper.IS_PARTICLE_EFFECTS_LOADED.get()) {
                if (subtleEffects$waitedTime < 10) {
                    return;
                }

                if (subtleEffects$me.getRandom().nextBoolean() && subtleEffects$isParticleEffectsParticle(options)) {
                    options = ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(), -1);
                }
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 3))
    private void replaceNonEffectParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ENTITIES.dragonsBreathClouds) {
            if (options.getType() == ParticleTypes.DRAGON_BREATH) {
                RandomSource random = subtleEffects$me.getRandom();
                if (subtleEffects$spawnParticles(level,
                        Mth.nextFloat(random, 0.7176471F, 0.8745098F),
                        0,
                        Mth.nextFloat(random, 0.8235294F, 0.9764706F),
                        x, y, z, 35, 15)
                ) {
                    return;
                }
            }
        }

        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            if (CompatHelper.IS_PARTICLE_EFFECTS_LOADED.get()) {
                if (subtleEffects$isParticleEffectsParticle(options)) {
                    ParticleOptions particleOptions = subtleEffects$me.getEntityData().get(DATA_PARTICLE);

                    if (particleOptions instanceof ColorParticleOption colorOption) {
                        if (subtleEffects$spawnParticles(level,
                                colorOption.getRed(), colorOption.getGreen(), colorOption.getBlue(),
                                x, y, z, 20, 24)) {
                            return;
                        }
                    }
                }
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Unique
    private boolean subtleEffects$spawnParticles(Level level, float red, float green, float blue, double x, double y, double z, int density, int waitingDensity) {
        boolean isWaiting = isWaiting();
        if (isWaiting && subtleEffects$waitedTime < 10) {
            return true;
        }

        RandomSource random = subtleEffects$me.getRandom();
        if (random.nextInt(isWaiting ? waitingDensity : density) == 0) {
            if (isWaiting) {
                level.addParticle(ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(), red, green, blue),
                        x, y, z, 0, 0, 0
                );
            }
            else {
                if (subtleEffects$me.tickCount <= 5) {
                    return true;
                }

                level.addAlwaysVisibleParticle(
                        ColorParticleOption.create(ModParticles.POTION_CLOUD.get(), red, green, blue),
                        x, subtleEffects$me.getY() + (random.nextDouble() / 10), z, 0, 0, 0
                );
            }
        }

        return !isWaiting && random.nextInt(20) > 0;
    }

    @Unique
    private static boolean subtleEffects$isParticleEffectsParticle(ParticleOptions options) {
        ResourceLocation id = BuiltInRegistries.PARTICLE_TYPE.getKey(options.getType());
        return id != null && id.getNamespace().equals(CompatHelper.PARTICLE_EFFECTS_MOD_ID);
    }
}

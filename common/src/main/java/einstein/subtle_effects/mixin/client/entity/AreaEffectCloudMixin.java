package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
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

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void cancelVanillaParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
    }

    // not the best way to backport this, but i don't care enough to figure it out properly
    @WrapOperation(method = "tick",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/core/particles/ParticleOptions;getType()Lnet/minecraft/core/particles/ParticleType;"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
                    )
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/particles/ParticleOptions;getType()Lnet/minecraft/core/particles/ParticleType;")
    )
    private ParticleType<?> replaceParticles(ParticleOptions options, Operation<ParticleType<?>> original, @Local(ordinal = 1) float radius) {
        Level level = subtleEffects$me.level();
        RandomSource random = level.getRandom();
        boolean isWaiting = subtleEffects$me.isWaiting();
        float f2 = random.nextFloat() * ((float) Math.PI * 2F);
        float f3 = Mth.sqrt(random.nextFloat()) * radius;
        double x = subtleEffects$me.getX() + (double) (Mth.cos(f2) * f3);
        double y = subtleEffects$me.getY();
        double z = subtleEffects$me.getZ() + (double) (Mth.sin(f2) * f3);

        if (options.getType() == ParticleTypes.ENTITY_EFFECT) {
            int color = subtleEffects$me.getColor();
            double red = (color >> 16 & 0xFF) / 255F;
            double green = (color >> 8 & 0xFF) / 255F;
            double blue = (color & 0xFF) / 255F;

            if (isWaiting && random.nextBoolean()) {
                replaceWaitingParticles(level, options, x, y, z, -1, -1, -1);
            }
            else {
                replaceEffectParticles(level, options, x, y, z, red, green, blue);
            }
        }
        else if (isWaiting) {
            level.addAlwaysVisibleParticle(options, x, y, z, 0, 0, 0);
        }
        else {
            replaceDragonBreathParticles(level, options, x, y, z, 0, 0, 0);
        }
        return null;
    }

    private void replaceWaitingParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            if (subtleEffects$waitedTime < 10) {
                return;
            }

            options = new ColorParticleOptions(ModParticles.POTION_POOF_CLOUD.get(), new Vector3f(-1, -1, -1));
        }
        level.addAlwaysVisibleParticle(options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    private void replaceEffectParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if (ModConfigs.ITEMS.lingeringPotionClouds) {
            boolean isWaiting = isWaiting();
            if (isWaiting && subtleEffects$waitedTime < 10) {
                return;
            }

            RandomSource random = level.getRandom();

            if (random.nextInt(isWaiting ? 4 : 20) == 0) {
                Vector3f color = new Vec3(xSpeed, ySpeed, zSpeed).toVector3f();
                if (isWaiting) {
                    level.addParticle(new ColorParticleOptions(ModParticles.POTION_POOF_CLOUD.get(), color),
                            x, y, z, 0, 0, 0
                    );
                }
                else {
                    subtleEffects$spawnPotionCloud(level, random, x, z, color);
                }
            }

            if (!isWaiting && random.nextInt(20) > 0) {
                return;
            }
        }
        level.addAlwaysVisibleParticle(options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    private void replaceDragonBreathParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        RandomSource random = level.getRandom();
        if (ModConfigs.ENTITIES.dragonsBreathClouds) {
            if (options.getType() == ParticleTypes.DRAGON_BREATH) {

                if (random.nextInt(35) == 0) {
                    subtleEffects$spawnPotionCloud(level, random, x, z,
                            new Vector3f(
                                    Mth.nextFloat(random, 0.7176471F, 0.8745098F),
                                    0,
                                    Mth.nextFloat(random, 0.8235294F, 0.9764706F)
                            )
                    );
                }

                if (random.nextInt(20) > 0) {
                    return;
                }
            }
        }
        level.addAlwaysVisibleParticle(options, x, y, z, (0.5 - random.nextDouble()) * 0.15, 0.01F, (0.5 - random.nextDouble()) * 0.15);
    }

    @Unique
    private void subtleEffects$spawnPotionCloud(Level level, RandomSource random, double x, double z, Vector3f color) {
        level.addAlwaysVisibleParticle(
                new ColorParticleOptions(ModParticles.POTION_CLOUD.get(), color),
                x, subtleEffects$me.getY() + (random.nextDouble() / 10), z, 0, 0, 0
        );
    }
}

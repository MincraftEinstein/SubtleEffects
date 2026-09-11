package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

@Mixin(value = Arrow.class, priority = 999)
public abstract class ArrowMixin extends AbstractArrow {

    protected ArrowMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    // Doing a WrapOperation instead of a ModifyExpressionValue so that it is compatible with ParticleEffects
    @WrapOperation(method = "makeParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void replaceEffectParticles(Level level, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original, @Local(ordinal = 1) int color) {
        if (ITEMS.tippedArrowPotionClouds) {
            if (inGround ? random.nextInt(3) > 0 : random.nextBoolean()) {
                particleData = ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(), color);
            }
        }
        original.call(level, particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @ModifyExpressionValue(method = "handleEntityEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;ENTITY_EFFECT:Lnet/minecraft/core/particles/ParticleType;"))
    private ParticleType<ColorParticleOption> replaceEffectExpirationParticles(ParticleType<ColorParticleOption> original) {
        if (ITEMS.tippedArrowPotionClouds && random.nextBoolean()) {
            return ModParticles.POTION_POOF_CLOUD.get();
        }
        return original;
    }

    @ModifyExpressionValue(method = {"makeParticle", "handleEntityEvent"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Arrow;getRandomY()D"))
    private double modifyParticleY(double original) {
        return ITEMS.tippedArrowPotionClouds ? getY(0.5F) : original;
    }
}

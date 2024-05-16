package einstein.ambient_sleep.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class CommonLivingEntityMixin {

    @Unique
    private final LivingEntity ambientSleep$me = (LivingEntity) (Object) this;

    @ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
    private int calculateFallDamage(int damage, float distance, float damageMultiplier) {
        ParticleSpawnUtil.spawnFallDustClouds(ambientSleep$me, damageMultiplier, damage);
        return damage;
    }
}

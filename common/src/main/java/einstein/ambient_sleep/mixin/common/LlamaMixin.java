package einstein.ambient_sleep.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Llama.class)
public class LlamaMixin {

    @Unique
    private final Llama ambientSleep$me = (Llama) (Object) this;

    @ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Llama;calculateFallDamage(FF)I"))
    private int calculateFallDamage(int damage, float distance, float damageMultiplier) {
        ParticleSpawnUtil.spawnFallDustClouds(ambientSleep$me, damageMultiplier, damage);
        return damage;
    }
}

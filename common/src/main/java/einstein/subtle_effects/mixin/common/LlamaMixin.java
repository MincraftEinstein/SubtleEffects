package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPayload;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Llama.class)
public class LlamaMixin {

    @Unique
    private final Llama subtleEffects$me = (Llama) (Object) this;

    @ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Llama;calculateFallDamage(FF)I"))
    private int calculateFallDamage(int damage, float distance, float damageMultiplier) {
        if (subtleEffects$me.isInvisible()) {
            ParticleSpawnUtil.spawnFallDustClouds(subtleEffects$me, damageMultiplier, damage, ClientBoundEntityFellPayload.TypeConfig.ENTITY);
        }
        return damage;
    }
}

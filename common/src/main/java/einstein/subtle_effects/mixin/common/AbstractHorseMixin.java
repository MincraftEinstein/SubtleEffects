package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {

    @Unique
    private final AbstractHorse subtleEffects$me = (AbstractHorse) (Object) this;

    protected AbstractHorseMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;calculateFallDamage(FF)I"))
    private int calculateFallDamage(int damage, float distance, float damageMultiplier) {
        ParticleSpawnUtil.spawnFallDustClouds(subtleEffects$me, damageMultiplier, damage);
        return damage;
    }

    @Inject(method = "tickRidden", at = @At("TAIL"))
    private void tickRidden(Player player, Vec3 travelVector, CallbackInfo ci) {
        if (level().isClientSide && ModConfigs.ENTITIES.dustClouds.mobRunning) {
            if (travelVector.z > 0 && onGround() && !(subtleEffects$me instanceof Camel)) {
                ParticleSpawnUtil.spawnCreatureMovementDustClouds(subtleEffects$me, level(), random, 20);
            }
        }
    }
}

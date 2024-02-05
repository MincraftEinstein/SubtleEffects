package einstein.ambient_sleep.mixin.common;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.Util;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {

    @Unique
    private final AbstractHorse ambientSleep$me = (AbstractHorse) (Object) this;

    protected AbstractHorseMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;calculateFallDamage(FF)I"))
    private int calculateFallDamage(AbstractHorse horse, float distance, float damageMultiplier) {
        int fallDamage = calculateFallDamage(distance, damageMultiplier);
        Util.spawnFallDustClouds(horse, distance, fallDamage);
        return fallDamage;
    }

    @Inject(method = "tickRidden", at = @At("TAIL"))
    private void tickRidden(Player player, Vec3 travelVector, CallbackInfo ci) {
        if (level().isClientSide && ModConfigs.INSTANCE.mobSprintingDustClouds.get()) {
            if (travelVector.z > 0 && onGround() && !(ambientSleep$me instanceof Camel)) {
                Util.spawnCreatureMovementDustClouds(ambientSleep$me, level(), random, 20);
            }
        }
    }
}

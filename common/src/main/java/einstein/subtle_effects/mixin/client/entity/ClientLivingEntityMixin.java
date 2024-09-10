package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import einstein.subtle_effects.init.ModDamageListeners;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntitySpawnSprintingDustCloudsPacket;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.EntityProvider;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin<T extends Entity> extends Entity {

    @Shadow
    public int hurtTime;

    @Unique
    private boolean subtleEffects$validEntity;

    @Unique
    private float subtleEffects$minSpeed;

    @Unique
    private boolean subtleEffects$canStart = true;

    @Unique
    private Vec3 subtleEffects$lastCheckedPos;

    @SuppressWarnings("all")
    @Unique
    private final LivingEntity subtleEffects$me = (LivingEntity) (Object) this;

    public ClientLivingEntityMixin(EntityType<T> type, Level level) {
        super(type, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<T> type, Level level, CallbackInfo ci) {
        if (subtleEffects$me instanceof Ravager) {
            subtleEffects$validEntity = true;
            subtleEffects$minSpeed = 0.34F;
        }

        if (subtleEffects$me instanceof Goat) {
            subtleEffects$validEntity = true;
            subtleEffects$minSpeed = 0.4F;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!level().isClientSide) {
            if (subtleEffects$canStart || position().distanceToSqr(subtleEffects$lastCheckedPos) > 0.5) {
                if (subtleEffects$validEntity) {
                    if (subtleEffects$me.getSpeed() > subtleEffects$minSpeed && onGround()) {
                        Services.NETWORK.sendToClientsTracking((ServerLevel) level(), subtleEffects$me.blockPosition(), new ClientBoundEntitySpawnSprintingDustCloudsPacket(getId()));
                        subtleEffects$lastCheckedPos = position();
                        subtleEffects$canStart = false;
                        return;
                    }
                    subtleEffects$lastCheckedPos = Vec3.ZERO;
                    subtleEffects$canStart = true;
                }
            }
        }
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 0))
    private boolean cancelFlyIntoWallServerCheck(boolean original) {
        return false;
    }

    @WrapWithCondition(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private boolean cancelFlyIntoWallClientSound(LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        return !level().isClientSide;
    }

    @WrapWithCondition(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean cancelFlyIntoWallClientHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof Player player && player.isCreative())) {
            ParticleSpawnUtil.spawnFallDustClouds(entity, 10, 10);
        }
        return !level().isClientSide;
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "hurt", at = @At("HEAD"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (level().isClientSide && !isInvulnerableTo(source) && amount > 0) {
            if (source.getEntity() instanceof LivingEntity && isAlive() && hurtTime == 0) {
                EntityType<T> type = (EntityType<T>) getType();
                if (ModDamageListeners.REGISTERED.containsKey(type)) {
                    ((EntityProvider<T>) ModDamageListeners.REGISTERED.get(type)).apply((T) this, level(), random);
                }
            }
        }
    }
}
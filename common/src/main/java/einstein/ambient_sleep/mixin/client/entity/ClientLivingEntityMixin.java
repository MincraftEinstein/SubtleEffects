package einstein.ambient_sleep.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import commonnetwork.api.Dispatcher;
import einstein.ambient_sleep.init.ModDamageListeners;
import einstein.ambient_sleep.networking.clientbound.ClientBoundEntitySpawnSprintingDustCloudsPacket;
import einstein.ambient_sleep.util.EntityProvider;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
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
    private boolean ambientSleep$validEntity;

    @Unique
    private float ambientSleep$minSpeed;

    @Unique
    private boolean ambientSleep$canStart = true;

    @Unique
    private Vec3 ambientSleep$lastCheckedPos;

    @SuppressWarnings("all")
    @Unique
    private final LivingEntity ambientSleep$me = (LivingEntity) (Object) this;

    public ClientLivingEntityMixin(EntityType<T> type, Level level) {
        super(type, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<T> type, Level level, CallbackInfo ci) {
        if (ambientSleep$me instanceof Ravager) {
            ambientSleep$validEntity = true;
            ambientSleep$minSpeed = 0.34F;
        }

        if (ambientSleep$me instanceof Goat) {
            ambientSleep$validEntity = true;
            ambientSleep$minSpeed = 0.4F;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!level().isClientSide) {
            if (ambientSleep$canStart || position().distanceToSqr(ambientSleep$lastCheckedPos) > 0.5) {
                if (ambientSleep$validEntity) {
                    if (ambientSleep$me.getSpeed() > ambientSleep$minSpeed && onGround()) {
                        Dispatcher.sendToAllClients(new ClientBoundEntitySpawnSprintingDustCloudsPacket(getId()), level().getServer());
                        ambientSleep$lastCheckedPos = position();
                        ambientSleep$canStart = false;
                        return;
                    }
                    ambientSleep$lastCheckedPos = Vec3.ZERO;
                    ambientSleep$canStart = true;
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
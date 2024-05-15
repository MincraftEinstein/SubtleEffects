package einstein.ambient_sleep.mixin.client.entity;

import einstein.ambient_sleep.init.ModDamageListeners;
import einstein.ambient_sleep.util.ParticleManager;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin<T extends Entity> extends Entity {

    @Shadow
    public int hurtTime;

    @SuppressWarnings("all")
    @Unique
    private final LivingEntity ambientSleep$me = (LivingEntity) (Object) this;

    public ClientLivingEntityMixin(EntityType<T> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 0))
    private boolean cancelFlyIntoWallServerCheck(Level level) {
        return false;
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void cancelFlyIntoWallClientSound(LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        if (!level().isClientSide) {
            playSound(sound, volume, pitch);
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean cancelFlyIntoWallClientHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!level().isClientSide) {
            return entity.hurt(source, amount);
        }

        if (entity instanceof Player player && player.isCreative()) {
            return false;
        }

        ParticleSpawnUtil.spawnFallDustClouds(entity, 10, 10);
        return false;
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "hurt", at = @At("HEAD"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (level().isClientSide && !isInvulnerableTo(source) && amount > 0) {
            if (source.getEntity() instanceof LivingEntity && isAlive() && hurtTime == 0) {
                EntityType<T> type = (EntityType<T>) getType();
                if (ModDamageListeners.REGISTERED.containsKey(type)) {
                    ((ParticleManager.EntityProvider<T>) ModDamageListeners.REGISTERED.get(type)).apply((T) this, level(), random);
                }
            }
        }
    }
}
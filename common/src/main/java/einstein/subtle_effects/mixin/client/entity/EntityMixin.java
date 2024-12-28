package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModDamageListeners;
import einstein.subtle_effects.util.EntityProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    protected abstract boolean isInvulnerableToBase(DamageSource p_20122_);

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @SuppressWarnings("unchecked")
    @Inject(method = "hurtOrSimulate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtClient(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public <T extends Entity> void hurtClient(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (subtleEffects$me instanceof LivingEntity entity && !isInvulnerableToBase(source)) {
            if (source.getEntity() instanceof LivingEntity && entity.isAlive() && entity.hurtTime == 0) {
                EntityType<T> type = (EntityType<T>) entity.getType();
                if (ModDamageListeners.REGISTERED.containsKey(type)) {
                    ((EntityProvider<T>) ModDamageListeners.REGISTERED.get(type)).apply((T) (Object) this, entity.level(), entity.getRandom());
                }
            }
        }
    }
}

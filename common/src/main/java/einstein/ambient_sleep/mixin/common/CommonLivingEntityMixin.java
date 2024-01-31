package einstein.ambient_sleep.mixin.common;

import commonnetwork.api.Dispatcher;
import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.ambient_sleep.util.ParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class CommonLivingEntityMixin extends Entity {

    public CommonLivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow
    protected abstract int calculateFallDamage(float distance, float damageMultiplier);

    @Redirect(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
    private int calculateFallDamage(LivingEntity entity, float distance, float damageMultiplier) {
        int fallDamage = calculateFallDamage(distance, damageMultiplier);
        if (level().isClientSide && entity.equals(Minecraft.getInstance().player)) {
            ParticleManager.entityFell(entity, entity.getY(), fallDamage);
        }
        else if (!level().isClientSide && !entity.equals(Minecraft.getInstance().player)) {
            Dispatcher.sendToAllClients(new ClientBoundEntityFellPacket(entity.getId(), fallDamage, entity.getY()), level().getServer());
        }
        return fallDamage;
    }
}

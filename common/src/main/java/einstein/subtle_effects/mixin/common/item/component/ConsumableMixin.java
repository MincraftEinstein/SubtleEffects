package einstein.subtle_effects.mixin.common.item.component;

import einstein.subtle_effects.networking.clientbound.ClientBoundDrankPotionPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public class ConsumableMixin {

    @Inject(method = "onConsume", at = @At(value = "HEAD"))
    private void spawnPotionParticles(Level level, LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (entity.isInvisible()) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            Services.NETWORK.sendToClientsTracking(entity instanceof ServerPlayer player ? player : null,
                    serverLevel, entity.blockPosition(), new ClientBoundDrankPotionPayload(entity.getId())
            );
            return;
        }
        ParticleSpawnUtil.spawnPotionRings(entity);

    }
}

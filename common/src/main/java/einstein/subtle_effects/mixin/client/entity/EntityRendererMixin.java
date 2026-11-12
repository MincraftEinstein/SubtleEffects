package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModRenderStateAttachmentKeys.*;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        RenderStateAttachmentAccessor accessor = (RenderStateAttachmentAccessor) state;
        if (entity instanceof LivingEntity livingEntity) {
            accessor.subtleEffects$set(IS_SLEEPING, livingEntity.isSleeping());
        }

        if (entity instanceof AbstractClientPlayer player) {
            accessor.subtleEffects$set(SOLAR_SYSTEM_SPIN, (player.tickCount + partialTicks) / 20F);
            accessor.subtleEffects$set(STRING_UUID, player.getStringUUID());
            accessor.subtleEffects$set(HEALTH_PERCENTAGE, player.getHealth() / player.getMaxHealth());
        }
    }
}

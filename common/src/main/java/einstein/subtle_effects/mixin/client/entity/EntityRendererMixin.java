package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModRenderStateAttachmentKeys.*;
import static einstein.subtle_effects.util.Util.getAdjustedNameTagPosition;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractRenderState(T entity, S renderState, float partialTicks, CallbackInfo ci) {
        RenderStateAttachmentAccessor accessor = (RenderStateAttachmentAccessor) renderState;
        if (entity instanceof LivingEntity livingEntity) {
            accessor.subtleEffects$set(IS_SLEEPING, livingEntity.isSleeping());
        }

        if (entity instanceof AbstractClientPlayer player) {
            accessor.subtleEffects$set(SOLAR_SYSTEM_SPIN, (player.tickCount + partialTicks) / 20F);
            accessor.subtleEffects$set(STRING_UUID, player.getStringUUID());
        }
    }

    @ModifyArg(method = "submitNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitNameTag(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;ILnet/minecraft/network/chat/Component;ZIDLnet/minecraft/client/renderer/state/CameraRenderState;)V"), index = 1)
    private Vec3 adjustNameTagOffset(Vec3 nameTagAttachment, @Local(argsOnly = true) EntityRenderState renderState) {
        return getAdjustedNameTagPosition(renderState, nameTagAttachment);
    }
}

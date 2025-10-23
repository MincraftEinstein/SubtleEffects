package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static einstein.subtle_effects.util.Util.getNameTagOffset;

@Mixin(value =  AvatarRenderer.class)
public class AvatarRendererMixin<T extends Entity, S extends EntityRenderState> {

    @ModifyArg(method = "submitNameTag(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitNameTag(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;ILnet/minecraft/network/chat/Component;ZIDLnet/minecraft/client/renderer/state/CameraRenderState;)V"), index = 1)
    private Vec3 translate(Vec3 nameTagAttachment, @Local(argsOnly = true) AvatarRenderState renderState) {
        if (nameTagAttachment == null) return null;
        if (!ModConfigs.ENTITIES.sleeping.adjustNameTagWhenSleeping) {
            return nameTagAttachment;
        }

        return getNameTagOffset(renderState, nameTagAttachment);
    }
}

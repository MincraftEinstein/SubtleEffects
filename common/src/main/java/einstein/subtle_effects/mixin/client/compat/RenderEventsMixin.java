package einstein.subtle_effects.mixin.client.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import de.maxhenkel.voicechat.voice.client.RenderEvents;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded(CompatHelper.SIMPLE_VC_MOD_ID)
@Mixin(value = RenderEvents.class, remap = false)
public class RenderEventsMixin {

    @WrapOperation(method = "onRenderName", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(Lnet/minecraft/world/phys/Vec3;)V"))
    private void renderNameTag(PoseStack poseStack, Vec3 nameTagPos, Operation<Void> original, EntityRenderState renderState) {
        original.call(poseStack, Util.adjustedNameTagPosition(renderState, nameTagPos));
    }
}

package einstein.subtle_effects.mixin.client.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import de.maxhenkel.voicechat.voice.client.RenderEvents;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded(CompatHelper.SIMPLE_VC_MOD_ID)
@Mixin(value = RenderEvents.class, remap = false)
public class RenderEventsMixin {

    @WrapOperation(method = "renderPlayerIcon", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 0, remap = true))
    private void renderNameTag(PoseStack poseStack, double x, double y, double z, Operation<Void> original, Player player) {
        double expectedYOffset = player.getBbHeight() + 0.5;
        float offsetY = player.getNameTagOffsetY();
        Vec3 nameTagPos = Util.adjustedNameTagPosition(player, new Vec3(x, (y != expectedYOffset ? y : (offsetY != expectedYOffset ? offsetY : y)), z));
        original.call(poseStack, nameTagPos.x(), nameTagPos.y(), nameTagPos.z());
    }
}

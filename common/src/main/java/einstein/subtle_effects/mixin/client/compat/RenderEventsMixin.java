package einstein.subtle_effects.mixin.client.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import de.maxhenkel.voicechat.voice.client.RenderEvents;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded(CompatHelper.SIMPLE_VC_MOD_ID)
@Mixin(value = RenderEvents.class, remap = false)
public class RenderEventsMixin {

    @WrapOperation(method = "renderPlayerIcon", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 0))
    private void adjustNameTagOffset(PoseStack poseStack, double x, double y, double z, Operation<Void> original, Player player) {
        Util.getAdjustedNameTagPosition(poseStack, x, y, z, original, player);
    }
}

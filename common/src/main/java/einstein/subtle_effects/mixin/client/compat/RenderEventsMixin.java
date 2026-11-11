package einstein.subtle_effects.mixin.client.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import de.maxhenkel.voicechat.voice.client.RenderEvents;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded(CompatHelper.SIMPLE_VC_MOD_ID)
@Mixin(value = RenderEvents.class, remap = false)
public class RenderEventsMixin {

    @ModifyExpressionValue(method = "renderPlayerIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityAttachments;getNullable(Lnet/minecraft/world/entity/EntityAttachment;IF)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 renderNameTag(Vec3 nameTagPos, Player player) {
        return Util.adjustedNameTagPosition(player, nameTagPos);
    }
}

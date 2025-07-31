package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer;
import einstein.subtle_effects.util.EntityAccessRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends HumanoidRenderState> {

    @Shadow
    @Final
    public ModelPart head;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void spinHead(T renderState, CallbackInfo ci) {
        EntityAccessRenderState accessor = (EntityAccessRenderState) renderState;
        Entity entity = accessor.subtleEffects$getEntity();

        if (entity instanceof AbstractClientPlayer player) {
            if (EinsteinSolarSystemLayer.shouldRender(player)) {
                float partialTicks = accessor.getPartialTick();

                head.yRot = EinsteinSolarSystemModel.getSpin(partialTicks, player, 0.5F);
            }
        }
    }
}

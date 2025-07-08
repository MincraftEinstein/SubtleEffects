package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart hat;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void spinHead(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayer player) {
            if (EinsteinSolarSystemLayer.shouldRender(player)) {
                float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(!entity.level().tickRateManager().isEntityFrozen(entity));
                float spin = EinsteinSolarSystemLayer.getSpin(partialTicks, player, 0.5F);

                head.yRot = spin;
                hat.yRot = spin;
            }
        }
    }
}

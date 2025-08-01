package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.EntityRenderStateAccessor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractRenderState(T entity, S renderState, float partialTicks, CallbackInfo ci) {
        EntityRenderStateAccessor accessor = (EntityRenderStateAccessor) renderState;
        if (entity instanceof LivingEntity livingEntity) {
            accessor.setSleeping(livingEntity.isSleeping());
        }

        if (entity instanceof AbstractClientPlayer player) {
            accessor.setShouldRenderSolarSystem(EinsteinSolarSystemLayer.shouldRender(player));
            accessor.setSolarSystemSpin((player.tickCount + partialTicks) / 20F);
        }
    }

    @WrapOperation(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private void translate(PoseStack poseStack, double x, double y, double z, Operation<Void> original, EntityRenderState renderState, @Local Vec3 nameTagPos) {
        if (!ModConfigs.ENTITIES.sleeping.adjustNameTagWhenSleeping) {
            original.call(poseStack, x, y, z);
            return;
        }

        if (renderState instanceof LivingEntityRenderState livingRenderState && ((EntityRenderStateAccessor) renderState).isSleeping()) {
            Direction facing = livingRenderState.bedOrientation;

            if (facing != null) {
                switch (facing) {
                    case NORTH -> poseStack.translate(z, x, -y);
                    case SOUTH -> poseStack.translate(z, x, y);
                    case EAST -> poseStack.translate(y, z, x);
                    case WEST -> poseStack.translate(-y, z, x);
                    default -> original.call(poseStack, x, y, z);
                }
            }
            return;
        }
        original.call(poseStack, x, y, z);
    }
}

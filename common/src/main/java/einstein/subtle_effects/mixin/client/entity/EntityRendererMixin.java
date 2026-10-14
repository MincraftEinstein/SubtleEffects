package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @WrapOperation(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private void adjustNameTagOffset(PoseStack poseStack, double x, double y, double z, Operation<Void> original, T entity) {
        Util.getAdjustedNameTagPosition(poseStack, x, y, z, original, entity);
    }
}

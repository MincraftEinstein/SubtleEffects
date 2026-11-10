package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @WrapOperation(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void renderNameTag(PoseStack poseStack, float x, float y, float z, Operation<Void> original, T entity) {
        Vec3 nameTagPos = Util.adjustedNameTagPosition(entity, new Vec3(x, y, z));
        original.call(poseStack, (float) nameTagPos.x(), (float) nameTagPos.y(), (float) nameTagPos.z());
    }
}

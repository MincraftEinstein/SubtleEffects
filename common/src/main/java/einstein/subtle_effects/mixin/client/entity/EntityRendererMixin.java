package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @ModifyExpressionValue(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityAttachments;getNullable(Lnet/minecraft/world/entity/EntityAttachment;IF)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 renderNameTag(Vec3 nameTagPos, T entity) {
        return Util.adjustedNameTagPosition(entity, nameTagPos);
    }
}

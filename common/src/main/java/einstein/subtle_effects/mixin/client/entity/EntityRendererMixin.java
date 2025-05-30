package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.EntityAccessRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractRenderState(T entity, S renderState, float partialTicks, CallbackInfo ci) {
        EntityAccessRenderState accessor = (EntityAccessRenderState) renderState;
        accessor.subtleEffects$setEntity(entity);
        accessor.setPartialTick(partialTicks);
    }

    @WrapOperation(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private void translate(PoseStack poseStack, double x, double y, double z, Operation<Void> original, EntityRenderState renderState, @Local Vec3 nameTagPos) {
        if (!ModConfigs.ENTITIES.sleeping.adjustNameTagWhenSleeping) {
            original.call(poseStack, x, y, z);
            return;
        }

        Entity entity = ((EntityAccessRenderState) renderState).subtleEffects$getEntity();
        if (entity instanceof LivingEntity livingEntity && livingEntity.isSleeping()) {
            Level level = entity.level();
            Optional<BlockPos> pos = livingEntity.getSleepingPos();

            if (pos.isPresent()) {
                BlockState state = level.getBlockState(pos.get());

                if (state.hasProperty(BedBlock.OCCUPIED) && state.getValue(BedBlock.OCCUPIED)) {
                    Direction facing = state.getValue(BedBlock.FACING);

                    switch (facing) {
                        case NORTH -> poseStack.translate(z, x, -y);
                        case SOUTH -> poseStack.translate(z, x, y);
                        case EAST -> poseStack.translate(y, z, x);
                        case WEST -> poseStack.translate(-y, z, x);
                        default -> original.call(poseStack, x, y, z);
                    }
                    return;
                }
            }
        }
        original.call(poseStack, x, y, z);
    }
}

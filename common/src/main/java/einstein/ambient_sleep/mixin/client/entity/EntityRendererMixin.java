package einstein.ambient_sleep.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.ambient_sleep.init.ModConfigs;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @WrapOperation(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void translate(PoseStack poseStack, float x, float y, float z, Operation<Void> original, T entity) {
        if (!ModConfigs.INSTANCE.adjustNametagWhenSleeping.get()) {
            original.call(poseStack, x, y, z);
            return;
        }

        if (entity instanceof LivingEntity livingEntity && livingEntity.isSleeping()) {
            Level level = entity.level();
            Optional<BlockPos> pos = livingEntity.getSleepingPos();

            if (pos.isPresent()) {
                BlockState state = level.getBlockState(pos.get());

                if (state.hasProperty(BedBlock.OCCUPIED) && state.getValue(BedBlock.OCCUPIED)) {
                    Direction facing = state.getValue(BedBlock.FACING);
                    float offsetY = entity.getNameTagOffsetY();
                    switch (facing) {
                        case NORTH -> poseStack.translate(0, 0.5, -offsetY);
                        case SOUTH -> poseStack.translate(0, 0.5, offsetY);
                        case EAST -> poseStack.translate(offsetY, 0.5, 0);
                        case WEST -> poseStack.translate(-offsetY, 0.5, 0);
                        default -> original.call(poseStack, x, y, z);
                    }
                    return;
                }
            }
        }
        original.call(poseStack, x, y, z);
    }
}

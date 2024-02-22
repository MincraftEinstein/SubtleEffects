package einstein.ambient_sleep.mixin.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import einstein.ambient_sleep.init.ModConfigs;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Unique
    private boolean ambientSleep$cancelTranslate = false;

    @Inject(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", shift = At.Shift.BEFORE))
    private void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource source, int packedLight, CallbackInfo ci) {
        ambientSleep$cancelTranslate = false;
        if (!ModConfigs.INSTANCE.adjustNametagRenderingWhenSleeping.get()) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity && livingEntity.isSleeping()) {
            Level level = entity.level();
            BlockPos belowPos = entity.blockPosition();
            BlockState state = level.getBlockState(belowPos);
            if (state.is(BlockTags.BEDS)) {
                Direction facing = state.getValue(BedBlock.FACING);
                float offsetY = entity.getNameTagOffsetY();
                double y = 0.5;
                switch (facing) {
                    case NORTH -> poseStack.translate(0, y, -offsetY);
                    case SOUTH -> poseStack.translate(0, y, offsetY);
                    case EAST -> poseStack.translate(offsetY, y, 0);
                    case WEST -> poseStack.translate(-offsetY, y, 0);
                    default -> poseStack.translate(0, offsetY, 0);
                }
                ambientSleep$cancelTranslate = true;
            }
        }
    }

    @Redirect(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void translate(PoseStack poseStack, float x, float y, float z) {
        if (ambientSleep$cancelTranslate) {
            poseStack.translate(0, 0, 0);
        }
        else {
            poseStack.translate(x, y, z);
        }
    }
}

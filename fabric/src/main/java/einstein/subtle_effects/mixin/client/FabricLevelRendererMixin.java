package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.client.renderer.ParticleBoundingBoxesRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class FabricLevelRendererMixin {

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldBorderRenderer;extract(Lnet/minecraft/world/level/border/WorldBorder;Lnet/minecraft/world/phys/Vec3;DLnet/minecraft/client/renderer/state/WorldBorderRenderState;)V", shift = At.Shift.AFTER))
    private void extractParticleBoundingBoxes(GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, Matrix4f frustumMatrix, Matrix4f projectionMatrix, Matrix4f cullingProjectionMatrix, GpuBufferSlice shaderFog, Vector4f fogColor, boolean renderSky, CallbackInfo ci, @Local Frustum frustum) {
        ParticleBoundingBoxesRenderer.extractParticleBoundingBoxes(levelRenderState, camera, frustum);
    }

    @Inject(method = "method_62213", at = @At(value = "TAIL"))
    private void renderParticleBoundingBoxes(GpuBufferSlice gpuBufferSlice, ResourceHandle<?> mainResourceHandle, ResourceHandle<?> particlesResourceHandle, CallbackInfo ci) {
        ParticleBoundingBoxesRenderer.renderParticleBoundingBoxes(new PoseStack(), levelRenderState);
    }
}

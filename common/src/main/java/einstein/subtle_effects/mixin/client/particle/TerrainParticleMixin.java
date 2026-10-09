package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(TerrainParticle.class)
abstract class TerrainParticleMixin extends TextureSheetParticle {

    @Mutable
    @Shadow
    @Final
    private float uo;
    @Mutable
    @Shadow
    @Final
    private float vo;
    @Unique
    private float subtleEffects$maxU = 1f;
    @Unique
    private float subtleEffects$maxV = 1f;

    protected TerrainParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
            at = @At("TAIL")
    )
    private void fixParticleUVs(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos, CallbackInfo ci) {
        // Make config
        var pixelPerPixel = 1;
        var denyList = new ArrayList<ResourceLocation>();
        var quadPixelSize = random.nextInt(1, 4); // move to in if

        var contents = sprite.contents();
        //noinspection ConstantValue
        if (!denyList.contains(contents.name())) {
            quadSize = quadPixelSize * (1f / 32f); // possibly add randomized scaler
            var spriteWidth = contents.width();
            var spireHeight = contents.height();
            var uStep = 1f / spriteWidth;
            var vStep = 1f / spireHeight;
            var pixelOffset = quadPixelSize * pixelPerPixel;
            uo = random.nextInt(0, spriteWidth - pixelOffset) * uStep;
            vo = random.nextInt(0, spireHeight - pixelOffset) * vStep;
            subtleEffects$maxU = uo + (pixelOffset * uStep);
            subtleEffects$maxV = vo + (pixelOffset * vStep);
        }
    }

    @ModifyReturnValue(method = "getU0", at = @At("RETURN"))
    private float u0(float original) {
        return sprite.getU(uo);
    }

    @ModifyReturnValue(method = "getU1", at = @At("RETURN"))
    private float u1(float original) {
        return sprite.getU(subtleEffects$maxU);
    }

    @ModifyReturnValue(method = "getV0", at = @At("RETURN"))
    private float v0(float original) {
        return sprite.getV(vo);
    }

    @ModifyReturnValue(method = "getV1", at = @At("RETURN"))
    private float v1(float original) {
        return sprite.getV(subtleEffects$maxV);
    }

}

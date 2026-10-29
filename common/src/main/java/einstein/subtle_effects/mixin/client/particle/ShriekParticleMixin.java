package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.particle.ShriekParticle;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.util.MathUtil.toRadians;

@Mixin(ShriekParticle.class)
public class ShriekParticleMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationX(F)Lorg/joml/Quaternionf;", remap = false))
    private Quaternionf modifyXRotation(Quaternionf quaternionf, float angle, Operation<Quaternionf> original) {
        return original.call(quaternionf, ModConfigs.GENERAL.disableShriekRotation ? toRadians(-90) : angle);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;", remap = false))
    private Quaternionf modifyYXZRotation(Quaternionf quaternionf, float angleY, float angleX, float angleZ, Operation<Quaternionf> original) {
        return ModConfigs.GENERAL.disableShriekRotation ? quaternionf.rotationX(toRadians(90)) : original.call(quaternionf, angleY, angleX, angleZ);
    }
}

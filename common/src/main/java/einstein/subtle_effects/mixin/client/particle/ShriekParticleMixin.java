package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.client.particle.ShriekParticle;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShriekParticle.class)
public class ShriekParticleMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationX(F)Lorg/joml/Quaternionf;"))
    private Quaternionf modifyXRotation(Quaternionf quaternionf, float angle, Operation<Quaternionf> original) {
        return original.call(quaternionf, ModConfigs.GENERAL.disableShriekRotation ? -90 * Mth.DEG_TO_RAD : angle);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"))
    private Quaternionf modifyYXZRotation(Quaternionf quaternionf, float angleY, float angleX, float angleZ, Operation<Quaternionf> original) {
        return ModConfigs.GENERAL.disableShriekRotation ? quaternionf.rotationX(90 * Mth.DEG_TO_RAD) : original.call(quaternionf, angleY, angleX, angleZ);
    }
}

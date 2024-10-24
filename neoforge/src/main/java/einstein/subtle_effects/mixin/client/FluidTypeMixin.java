package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FluidType.class, remap = false)
public class FluidTypeMixin {

    @WrapOperation(method = "onVaporize", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"), remap = false)
    private SimpleParticleType replaceSmoke(Operation<SimpleParticleType> original) {
        if (ModConfigs.ITEMS.waterEvaporateFromBucketSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }
}

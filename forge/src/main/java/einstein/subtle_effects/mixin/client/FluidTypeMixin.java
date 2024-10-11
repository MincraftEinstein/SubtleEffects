package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FluidType.class, remap = false)
public class FluidTypeMixin {

    @Redirect(method = "onVaporize", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"), remap = false)
    private SimpleParticleType replaceSmoke() {
        if (ModConfigs.ITEMS.waterEvaporateFromBucketSteam) {
            return ModParticles.STEAM.get();
        }
        return ParticleTypes.LARGE_SMOKE;
    }
}

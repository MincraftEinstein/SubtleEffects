package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.LightningRodBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {

    @ModifyExpressionValue(method = "animateTick",at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;ELECTRIC_SPARK:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceElectricSpark(SimpleParticleType original) {
        if (ModConfigs.BLOCKS.replaceCopperElectricitySparks) {
            return ModParticles.ELECTRICITY.get();
        }
        return original;
    }
}

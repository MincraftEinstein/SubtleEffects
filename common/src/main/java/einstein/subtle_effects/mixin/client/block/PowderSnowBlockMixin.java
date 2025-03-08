package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @ModifyExpressionValue(method = "entityInside", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SNOWFLAKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSnowflake(SimpleParticleType original) {
        if (ModConfigs.BLOCKS.replacePowderSnowFlakes) {
            return ModParticles.SNOW.get();
        }
        return original;
    }
}

package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.AbstractCandleBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCandleBlock.class)
public abstract class AbstractCandleBlockMixin {

    @WrapOperation(method = {"addParticlesAndSound", "extinguish"}, at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private static SimpleParticleType replaceSmoke(Operation<SimpleParticleType> original) {
        if (ModConfigs.BLOCKS.updatedSmoke.candleSmoke) {
            return ModParticles.SMOKE.get();
        }
        return original.call();
    }
}

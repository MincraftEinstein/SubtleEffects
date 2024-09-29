package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.AbstractCandleBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractCandleBlock.class)
public abstract class AbstractCandleBlockMixin {

    @Redirect(method = {"addParticlesAndSound", "extinguish"}, at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private static SimpleParticleType replaceSmoke() {
        if (ModConfigs.BLOCKS.updatedSmoke.candleSmoke) {
            return ModParticles.SMOKE.get();
        }
        return ParticleTypes.SMOKE;
    }
}

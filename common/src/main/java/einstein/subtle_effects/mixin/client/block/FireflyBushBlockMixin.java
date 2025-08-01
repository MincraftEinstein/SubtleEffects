package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.configs.environment.FireflyConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.FireflyBushBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireflyBushBlock.class)
public class FireflyBushBlockMixin {

    @WrapOperation(method = "animateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;FIREFLY:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceFirefly(Operation<SimpleParticleType> original) {
        if (ModConfigs.BLOCKS.fireflyBushFireflyType == FireflyConfigs.FireflyType.ORIGINAL) {
            return ModParticles.FIREFLY.get();
        }
        return original.call();
    }
}

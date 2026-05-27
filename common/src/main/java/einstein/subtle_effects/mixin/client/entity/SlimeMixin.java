package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Slime.class)
public class SlimeMixin {

    @ModifyReturnValue(method = "getParticleType", at = @At("RETURN"))
    private ParticleOptions getParticleType(ParticleOptions original) {
        if (ModConfigs.ENTITIES.replaceSlimeSquishParticles) {
            return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState());
        }
        return original;
    }
}

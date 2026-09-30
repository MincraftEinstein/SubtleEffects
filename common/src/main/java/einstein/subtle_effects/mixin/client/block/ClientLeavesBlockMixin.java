package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingParticlesLeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallingParticlesLeavesBlock.class)
public class ClientLeavesBlockMixin {

    @ModifyExpressionValue(method = "makeFallingLeavesParticles", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/FallingParticlesLeavesBlock;leafParticleChance:F"))
    private float replaceDecayTicks(float original, Level level, BlockPos pos) {
        if (ModConfigs.BLOCKS.rainIncreasesLeavesSpawningParticles) {
            if (Util.isRainingAt(level, pos)) {
                return original * 2;
            }
        }
        return original;
    }

}

package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LeavesBlock.class)
public class ClientLeavesBlockMixin {

    @ModifyExpressionValue(method = "makeFallingLeavesParticles", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/LeavesBlock;leafParticleChance:F"))
    private float replaceDecayTicks(float original, @Local(argsOnly = true) Level level, @Local(argsOnly = true, ordinal = 0) BlockPos pos) {
        if (ModConfigs.BLOCKS.rainIncreasesLeavesSpawningParticles) {
            if (Util.isRainingAt(level, pos)) {
                return original * 2;
            }
        }
        return original;
    }
}

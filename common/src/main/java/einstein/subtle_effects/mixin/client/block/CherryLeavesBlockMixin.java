package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CherryLeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CherryLeavesBlock.class)
public class CherryLeavesBlockMixin {

    @ModifyExpressionValue(method = "animateTick", at = @At(value = "CONSTANT", args = "intValue=10"))
    private int replaceDecayTicks(int original, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos) {
        if (ModConfigs.BLOCKS.rainIncreasesLeavesSpawningParticles) {
            if (Util.isRainingAt(level, pos)) {
                return 5;
            }
        }
        return original;
    }
}

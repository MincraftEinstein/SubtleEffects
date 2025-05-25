package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
public class ClientComposterBlockMixin {

    @Inject(method = "handleFill", at = @At("TAIL"))
    private static void handleFill(Level level, BlockPos pos, boolean success, CallbackInfo ci) {
        if (ModConfigs.BLOCKS.compostingParticles) {
            ParticleSpawnUtil.spawnCompostParticles(level, pos, ModParticles.COMPOST.get(), 0, 0, 0);
        }
    }
}

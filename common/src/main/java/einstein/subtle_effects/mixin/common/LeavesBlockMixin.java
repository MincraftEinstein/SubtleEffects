package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundBlockDestroyEffectsPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        Services.NETWORK.sendToClientsTracking(level, pos, new ClientBoundBlockDestroyEffectsPayload(state, pos, ClientBoundBlockDestroyEffectsPayload.TypeConfig.LEAVES_DECAY));
    }
}

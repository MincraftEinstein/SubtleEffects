package einstein.subtle_effects.mixin.common.block;

import einstein.subtle_effects.networking.clientbound.ClientBoundBlockDestroyEffectsPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {

    @Inject(method = "turnToDirt", at = @At("TAIL"))
    private static void turnToDirt(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        Services.NETWORK.sendToClientsTracking((ServerLevel) level, pos, new ClientBoundBlockDestroyEffectsPayload(state, pos, ClientBoundBlockDestroyEffectsPayload.TypeConfig.FARMLAND_DESTROY));
    }
}

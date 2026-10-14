package einstein.subtle_effects.mixin.common.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.PayloadSender;
import einstein.subtle_effects.networking.clientbound.ClientBoundDecoratedPotInteractionPayload;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {

    @WrapOperation(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private <T extends ParticleOptions> int useItemOn(ServerLevel level, T type, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed, Operation<Integer> original, @Local(argsOnly = true) BlockPos pos) {
        if (Util.isUnderwater(level, pos)) {
            if (!Util.isAboveDownwardsBubbleColumn(level, pos)) {
                PayloadSender.sendToClientsTracking(null, level, pos, new ClientBoundDecoratedPotInteractionPayload(pos), skippedPlayer ->
                        level.sendParticles(skippedPlayer, type, false, posX, posY, posZ, particleCount, xOffset, yOffset, zOffset, speed)
                );
                return -1;
            }
        }
        return original.call(level, type, posX, posY, posZ,  particleCount, xOffset, yOffset, zOffset, speed);
    }

    @Inject(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;wobble(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity$WobbleStyle;)V"))
    private void useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level instanceof ServerLevel serverLevel) {
            if (Util.isUnderwater(level, pos)) {
                if (Util.isAboveDownwardsBubbleColumn(level, pos)) {
                    return;
                }

                PayloadSender.sendToClientsTracking(serverLevel, pos, new ClientBoundDecoratedPotInteractionPayload(pos));
            }
        }
    }
}

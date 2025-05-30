package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundSpawnSnoreParticlePayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {

    @Unique
    private int subtleEffects$breatheTimer = 0;

    @Unique
    private int subtleEffects$snoreTimer = 0;

    @Unique
    private int subtleEffects$ZCount = 0;

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void serverTick(Level level, BlockPos pos, BlockState state, BeehiveBlockEntity blockEntity, CallbackInfo ci) {
        BeehiveBlockEntityMixin me = (BeehiveBlockEntityMixin) (Object) blockEntity;
        if (!blockEntity.isEmpty() && level.isNight()) {
            if (me.subtleEffects$breatheTimer < Util.BREATH_DELAY) {
                me.subtleEffects$breatheTimer++;
                return;
            }

            if (me.subtleEffects$snoreTimer >= Util.SNORE_DELAY) {
                me.subtleEffects$snoreTimer = 0;
                me.subtleEffects$ZCount++;
                Direction direction = state.getValue(BeehiveBlock.FACING);
                Services.NETWORK.sendToClientsTracking((ServerLevel) level, pos, new ClientBoundSpawnSnoreParticlePayload(pos.getX() + 0.5 + (0.6 * direction.getStepX()), pos.getY() + 0.5, pos.getZ() + 0.5 + (0.6 * direction.getStepZ())));

                if (me.subtleEffects$ZCount >= Util.MAX_Z_COUNT) {
                    me.subtleEffects$ZCount = 0;
                    me.subtleEffects$breatheTimer = 0;
                }
            }

            if (me.subtleEffects$snoreTimer < Util.SNORE_DELAY) {
                me.subtleEffects$snoreTimer++;
            }
        }
        else {
            me.subtleEffects$breatheTimer = 0;
            me.subtleEffects$snoreTimer = 0;
            me.subtleEffects$ZCount = 0;
        }
    }
}

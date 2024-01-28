package einstein.ambient_sleep.mixin.common;

import commonnetwork.api.Dispatcher;
import einstein.ambient_sleep.networking.clientbound.ClientBoundSpawnSnoreParticlePacket;
import einstein.ambient_sleep.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
    private int ambientSleep$breatheTimer = 0;

    @Unique
    private int ambientSleep$snoreTimer = 0;

    @Unique
    private int ambientSleep$ZCount = 0;

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void serverTick(Level level, BlockPos pos, BlockState state, BeehiveBlockEntity blockEntity, CallbackInfo ci) {
        BeehiveBlockEntityMixin me = (BeehiveBlockEntityMixin) (Object) blockEntity;
        if (!blockEntity.isEmpty() && level.isNight()) {
            if (me.ambientSleep$breatheTimer < Util.BREATH_DELAY) {
                me.ambientSleep$breatheTimer++;
                return;
            }

            if (me.ambientSleep$snoreTimer >= Util.SNORE_DELAY) {
                me.ambientSleep$snoreTimer = 0;
                me.ambientSleep$ZCount++;
                Direction direction = state.getValue(BeehiveBlock.FACING);
                Dispatcher.sendToAllClients(new ClientBoundSpawnSnoreParticlePacket(pos.getX() + 0.5 + (0.6 * direction.getStepX()), pos.getY() + 0.5, pos.getZ() + 0.5 + (0.6 * direction.getStepZ())), level.getServer());

                if (me.ambientSleep$ZCount >= Util.MAX_Z_COUNT) {
                    me.ambientSleep$ZCount = 0;
                    me.ambientSleep$breatheTimer = 0;
                }
            }

            if (me.ambientSleep$snoreTimer < Util.SNORE_DELAY) {
                me.ambientSleep$snoreTimer++;
            }
        }
        else {
            me.ambientSleep$breatheTimer = 0;
            me.ambientSleep$snoreTimer = 0;
            me.ambientSleep$ZCount = 0;
        }
    }
}

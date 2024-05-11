package einstein.ambient_sleep.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.ambient_sleep.mixin.client.entity.EntityAccessor;
import einstein.ambient_sleep.util.ParticleManager;
import einstein.ambient_sleep.tickers.TickerManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @WrapOperation(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void entityTick(Entity entity, Operation<Void> original) {
        original.call(entity);
        if (((EntityAccessor) entity).getTickCount() < 2) { // 2 because the first tick has already happened
            TickerManager.createTickersForEntity(entity);
        }
    }

    @WrapOperation(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V"))
    private void entityRideTick(Entity entity, Operation<Void> original) {
        original.call(entity);
        if (((EntityAccessor) entity).getTickCount() < 2) {
            TickerManager.createTickersForEntity(entity);
        }
    }

    @WrapOperation(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void animateTick(Block block, BlockState state, Level level, BlockPos pos, RandomSource random, Operation<Void> original) {
        original.call(block, state, level, pos, random);
        if (!state.isAir()) {
            ParticleManager.blockAnimateTick(state, level, pos, random);
        }
    }
}

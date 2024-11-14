package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.tickers.TickerManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionType, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionType, isClientSide, isDebug, biomeZoomSeed, maxNeighborUpdates);
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void entityTick(Entity entity, CallbackInfo ci) {
        TickerManager.createTickersForEntity(entity);
    }

    @Inject(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V"))
    private void entityRideTick(Entity vehicleEntity, Entity entity, CallbackInfo ci) {
        TickerManager.createTickersForEntity(entity);
    }

    @Inject(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void animateTick(int x, int y, int z, int range, RandomSource random, Block markerBlock, BlockPos.MutableBlockPos pos, CallbackInfo ci) {
        BlockState state = getBlockState(pos);
        if (!state.isAir()) {
            ModBlockTickers.REGISTERED.forEach((predicate, provider) -> {
                if (predicate.test(state)) {
                    provider.apply(state, this, pos, random);
                }
            });
        }
    }
}

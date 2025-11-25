package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.ticking.FireflyManager;
import einstein.subtle_effects.ticking.GeyserManager;
import einstein.subtle_effects.ticking.SparkProviderManager;
import einstein.subtle_effects.ticking.tickers.ChestBlockEntityTicker;
import einstein.subtle_effects.ticking.tickers.WaterfallTicker;
import einstein.subtle_effects.ticking.tickers.entity.EntityTickerManager;
import einstein.subtle_effects.util.BlockTickerProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    @Shadow
    @Final
    @Mutable
    private static Set<Item> MARKER_PARTICLE_ITEMS;

    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionType, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionType, profiler, isClientSide, isDebug, biomeZoomSeed, maxNeighborUpdates);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (ModConfigs.ITEMS.structureVoidItemMarker) {
            Set<Item> markerItems = new HashSet<>(MARKER_PARTICLE_ITEMS);
            markerItems.add(Items.STRUCTURE_VOID);
            MARKER_PARTICLE_ITEMS = Set.copyOf(markerItems);
        }
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void entityTick(Entity entity, CallbackInfo ci) {
        EntityTickerManager.createTickersForEntity(entity);
    }

    @Inject(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V"))
    private void entityRideTick(Entity vehicleEntity, Entity entity, CallbackInfo ci) {
        EntityTickerManager.createTickersForEntity(entity);
    }

    @Inject(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void animateTick(int x, int y, int z, int range, RandomSource random, Block markerBlock, BlockPos.MutableBlockPos mutablePos, CallbackInfo ci) {
        BlockPos pos = mutablePos.immutable();
        BlockState state = getBlockState(pos);
        Block block = state.getBlock();

        FireflyManager.tick(this, pos, state, random);

        if (!state.isAir()) {
            BlockTickerProvider tickerProvider = ModBlockTickers.REGISTERED.get(block);

            if (tickerProvider != null) {
                tickerProvider.apply(state, this, pos, random);
            }

            GeyserManager.tick(this, state, pos);
            ChestBlockEntityTicker.trySpawn(this, pos);

            FluidState fluidState = getFluidState(pos);
            if (!fluidState.isEmpty()) {
                WaterfallTicker.trySpawn(this, fluidState, pos);
            }

            ModBlockTickers.REGISTERED_SPECIAL.forEach((predicate, provider) -> {
                if (predicate.test(state)) {
                    provider.apply(state, this, pos, random);
                }
            });

            SparkProviderManager.tick(this, random, block, state, pos);
        }
    }
}

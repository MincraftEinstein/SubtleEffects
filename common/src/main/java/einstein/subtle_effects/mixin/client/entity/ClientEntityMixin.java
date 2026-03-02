package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import einstein.subtle_effects.util.EntityTickerAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.util.MathUtil.nextDouble;

@Mixin(Entity.class)
public abstract class ClientEntityMixin implements EntityTickerAccessor, FluidLogicAccessor {

    @Shadow
    @Final
    protected RandomSource random;
    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Unique
    private final Int2ObjectMap<EntityTicker<?>> subtleEffects$tickers = new Int2ObjectOpenHashMap<>();

    @Unique
    private double subtleEffects$nextCobwebSound = 0.5;

    @Unique
    private Vec3 subtleEffects$lastPos = Vec3.ZERO;

    @Shadow
    public abstract Level level();

    @Shadow
    protected boolean firstTick;

    @Unique
    @Nullable
    private FluidDefinition subtleEffects$lastTouchedFluid;

    @Unique
    private boolean subtleEffects$cancelWaterSplash = false;

    @Inject(method = "playEntityOnFireExtinguishedSound", at = @At("TAIL"))
    private void addExtinguishParticles(CallbackInfo ci) {
        Level level = subtleEffects$me.level();

        if (level.isClientSide() && ModConfigs.ENTITIES.burning.extinguishSteam) {
            AABB boundingBox = subtleEffects$me.getBoundingBox();

            for (int x = Mth.floor(boundingBox.minX); x < Mth.ceil(boundingBox.maxX); x++) {
                for (int y = Mth.floor(boundingBox.minY); y < Mth.ceil(boundingBox.maxY); y++) {
                    for (int z = Mth.floor(boundingBox.minZ); z < Mth.ceil(boundingBox.maxZ); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        FluidState fluidState = level.getFluidState(pos);
                        double fluidHeight = Math.max(Util.getCauldronFillHeight(state), fluidState.getHeight(level, pos));

                        if (fluidHeight > 0 && (fluidState.is(Fluids.WATER) || state.is(Blocks.WATER_CAULDRON))) {
                            BlockPos abovePos = pos.above();

                            if (!Util.isSolidOrNotEmpty(level, abovePos)) {

                                // More particles than 5 will be displayed, due to this method being called by multiple ticks
                                for (int i = 0; i < 5; i++) {
                                    level.addParticle(ModParticles.STEAM.get(),
                                            pos.getX() + random.nextDouble(),
                                            pos.getY() + fluidHeight + nextDouble(random, 0.5),
                                            pos.getZ() + random.nextDouble(),
                                            0, 0, 0
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "onInsideBlock", at = @At("HEAD"))
    private void inside(BlockState state, CallbackInfo ci) {
        if (subtleEffects$me.level().isClientSide) {
            if (ModConfigs.BLOCKS.cobwebMovementSounds) {
                if (subtleEffects$me.flyDist > subtleEffects$nextCobwebSound && state.is(Blocks.COBWEB)) {
                    if (subtleEffects$lastPos.distanceToSqr(subtleEffects$me.position()) > 0.5) {
                        subtleEffects$nextCobwebSound += 0.5;
                        subtleEffects$lastPos = subtleEffects$me.position();

                        SoundType soundType = state.getSoundType();
                        Util.playClientSound(subtleEffects$me, soundType.getStepSound(), subtleEffects$me.getSoundSource(), soundType.getVolume() * 0.15F, soundType.getPitch());
                    }
                }
            }
        }
    }

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("HEAD"))
    private void clearFluidDefinitionHeight(CallbackInfoReturnable<Boolean> cir) {
        subtleEffects$getFluidDefinitionHeight().clear();
    }

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("TAIL"))
    private void preformSplash(CallbackInfoReturnable<Boolean> cir) {
        subtleEffects$lastTouchedFluid = ParticleSpawnUtil.preformSplash(false, false, subtleEffects$me, firstTick, isWater -> {
        });
    }

    @WrapOperation(method = "updateInWaterStateAndDoWaterCurrentPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z"))
    private boolean preformWaterSplash(Entity instance, TagKey<Fluid> fluidTag, double motionScale, Operation<Boolean> original) {
        boolean result = original.call(instance, fluidTag, motionScale);

        if (result) {
            subtleEffects$lastTouchedFluid = ParticleSpawnUtil.preformSplash(true, false, subtleEffects$me, firstTick, isWater -> {
                if (isWater) {
                    subtleEffects$cancelWaterSplash = true;
                }
            });
        }

        return result;
    }

    @Inject(method = "doWaterSplashEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I"), cancellable = true)
    private void cancelWaterSplash(CallbackInfo ci) {
        if (level().isClientSide() && subtleEffects$cancelWaterSplash) {
            ci.cancel();
        }
        subtleEffects$cancelWaterSplash = false;
    }

    @Nullable
    @Override
    public FluidDefinition subtleEffects$getLastTouchedFluid() {
        return subtleEffects$lastTouchedFluid;
    }

    @Override
    public void subtleEffects$setLastTouchedFluid(@Nullable FluidDefinition fluidDefinition) {
        subtleEffects$lastTouchedFluid = fluidDefinition;
    }

    @Override
    public void subtleEffects$cancelNextWaterSplash() {
        subtleEffects$cancelWaterSplash = true;
    }

    @Override
    public Int2ObjectMap<EntityTicker<?>> subtleEffects$getTickers() {
        return subtleEffects$tickers;
    }
}

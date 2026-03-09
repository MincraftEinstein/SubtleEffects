package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.InterimCalculationAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public abstract class NeoForgeClientEntityMixin implements FluidLogicAccessor {

    @Shadow
    protected boolean firstTick;

    @Unique
    private final Object2DoubleMap<FluidDefinition> subtleEffects$fluidPairHeight = new Object2DoubleArrayMap<>();

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Inject(method = "playStepSound", at = @At(value = "HEAD"), cancellable = true)
    private void stepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (subtleEffects$me instanceof SnowGolem && ModConfigs.ENTITIES.snowGolemStepSounds) {
            SoundType soundType = SoundType.SNOW;
            subtleEffects$me.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
            ci.cancel();
        }
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private void storeFluidPairHeight(CallbackInfo ci, @Local FluidState fluidState, @Share("fluidPairs") LocalRef<Map<FluidType, FluidDefinition>> fluidPairsRef) {
        FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
        FluidType type = fluidState.getFluidType();
        Map<FluidType, FluidDefinition> fluidTypePairs = fluidPairsRef.get();
        if (fluidTypePairs == null) {
            fluidTypePairs = new HashMap<>();
        }

        if (!fluidTypePairs.containsKey(type) && fluidDefinition != null) {
            fluidTypePairs.put(type, fluidDefinition);
        }

        fluidPairsRef.set(fluidTypePairs);
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Z)V", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;forEach(Ljava/util/function/BiConsumer;)V", remap = false))
    private void updateFluidPairHeight(CallbackInfo ci, @Local Object2ObjectMap<FluidType, Object> interimCalcs, @Share("fluidPairs") LocalRef<Map<FluidType, FluidDefinition>> fluidPairsRef) {
        interimCalcs.forEach((type, interim) -> {
            FluidDefinition fluidDefinition = fluidPairsRef.get().get(type);

            if (fluidDefinition != null) {
                subtleEffects$fluidPairHeight.put(fluidDefinition, ((InterimCalculationAccessor) interim).subtleEffects$getFluidHeight());
            }
        });
    }

    @WrapOperation(method = "updateInWaterStateAndDoWaterCurrentPushing(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateFluidHeightAndDoCanPushEntityFluidPushing(Z)Z"))
    private boolean preformWaterSplash(Entity instance, boolean preformFluidPushing, Operation<Boolean> original) {
        boolean result = original.call(instance, preformFluidPushing);

        if (result) {
            subtleEffects$setLastTouchedFluid(ParticleSpawnUtil.preformSplash(true, false, subtleEffects$me, firstTick, isWater -> {
                if (isWater) {
                    subtleEffects$cancelNextWaterSplash();
                }
            }));
        }

        return result;
    }

    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidPairHeight;
    }

    // For some reason using an actual mixin accessor didn't work, idk why
    @Mixin(targets = "net.minecraft.world.entity.Entity$1InterimCalculation", remap = false)
    public static class InterimCalculationMixin implements InterimCalculationAccessor {

        @Shadow
        double fluidHeight;

        @Override
        public double subtleEffects$getFluidHeight() {
            return fluidHeight;
        }
    }
}

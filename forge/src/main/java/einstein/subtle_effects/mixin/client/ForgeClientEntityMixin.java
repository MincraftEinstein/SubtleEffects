package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@Mixin(Entity.class)
public abstract class ForgeClientEntityMixin implements FluidLogicAccessor {

    @Shadow
    protected boolean firstTick;

    @Unique
    private final Object2DoubleMap<FluidDefinition> subtleEffects$fluidPairHeight = new Object2DoubleArrayMap<>();

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @ModifyExpressionValue(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"))
    private SoundType stepSound(SoundType soundType) {
        if (subtleEffects$me instanceof SnowGolem && ModConfigs.ENTITIES.snowGolemStepSounds) {
            return SoundType.SNOW;
        }
        return soundType;
    }

    @WrapOperation(method = "updateInWaterStateAndDoWaterCurrentPushing", at = @At(value = "INVOKE", target = "Ljava/util/function/BooleanSupplier;getAsBoolean()Z"))
    private boolean preformWaterSplash(BooleanSupplier instance, Operation<Boolean> original) {
        boolean result = original.call(instance);

        if (result) {
            subtleEffects$setLastTouchedFluid(ParticleSpawnUtil.preformSplash(true, false, subtleEffects$me, firstTick, isWater -> {
                if (isWater) {
                    subtleEffects$cancelNextWaterSplash();
                }
            }));
        }

        return result;
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", remap = true), remap = false)
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

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectArrayMap;forEach(Ljava/util/function/BiConsumer;)V"), remap = false)
    private void updateFluidPairHeight(CallbackInfo ci, @Local Object2ObjectArrayMap<FluidType, Entity.FluidCalcs> fluidCalcs, @Share("fluidPairs") LocalRef<Map<FluidType, FluidDefinition>> fluidPairsRef) {
        fluidCalcs.forEach((type, fluidCalc) -> {
            FluidDefinition fluidDefinition = fluidPairsRef.get().get(type);
// :forge-shredder: + check fabric and neo on all versions to make sure the mod menu mixins aren't causing remap crashes
            if (fluidDefinition != null) {
                subtleEffects$fluidPairHeight.put(fluidDefinition, fluidCalc.height);
            }
        });
    }

    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidPairHeight;
    }
}

package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.apache.commons.lang3.tuple.MutableTriple;
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

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"), remap = false)
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

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;forEach(Ljava/util/function/BiConsumer;)V"), remap = false)
    private void updateFluidPairHeight(CallbackInfo ci, @Local Object2ObjectMap<FluidType, MutableTriple<Double, Vec3, Integer>> interimCalcs, @Share("fluidPairs") LocalRef<Map<FluidType, FluidDefinition>> fluidPairsRef) {
        interimCalcs.forEach((type, interim) -> {
            FluidDefinition fluidDefinition = fluidPairsRef.get().get(type);

            if (fluidDefinition != null) {
                subtleEffects$fluidPairHeight.put(fluidDefinition, interim.getLeft());
            }
        });
    }

    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidPairHeight;
    }
}

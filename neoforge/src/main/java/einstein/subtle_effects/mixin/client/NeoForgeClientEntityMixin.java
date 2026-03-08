package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.InterimCalculationAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.block.SoundType;
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

    @Inject(method = "updateFluidHeightAndDoFluidPushing()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
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

    @Inject(method = "updateFluidHeightAndDoFluidPushing()V", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;forEach(Ljava/util/function/BiConsumer;)V", remap = false))
    private void updateFluidPairHeight(CallbackInfo ci, @Local Object2ObjectMap<FluidType, Object> interimCalcs, @Share("fluidPairs") LocalRef<Map<FluidType, FluidDefinition>> fluidPairsRef) {
        interimCalcs.forEach((type, interim) -> {
            FluidDefinition fluidDefinition = fluidPairsRef.get().get(type);

            if (fluidDefinition != null) {
                subtleEffects$fluidPairHeight.put(fluidDefinition, ((InterimCalculationAccessor) interim).subtleEffects$getFluidHeight());
            }
        });
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

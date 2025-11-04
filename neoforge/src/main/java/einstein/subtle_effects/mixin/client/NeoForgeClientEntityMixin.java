package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.util.*;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public class NeoForgeClientEntityMixin implements FluidHeightAccessor {

    @Shadow
    protected boolean firstTick;

    @Unique
    private final Entity subtleEffectsNeoForge$me = (Entity) (Object) this;

    @Unique
    private final Object2DoubleMap<FluidPair> subtleEffects$fluidPairHeight = new Object2DoubleArrayMap<>();

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("TAIL"))
    private void updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> cir) {
        subtleEffects$fluidPairHeight.clear();
        boolean isInLava = CommonUtil.isEntityInFluid(subtleEffectsNeoForge$me, FluidTags.LAVA);
        CommonEntityAccessor accessor = (CommonEntityAccessor) subtleEffectsNeoForge$me;
        ParticleSpawnUtil.spawnLavaSplash(subtleEffectsNeoForge$me, isInLava, firstTick, accessor.subtleEffects$wasTouchingLava());
        accessor.subtleEffects$setTouchingLava(isInLava);
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private void getFluidPairs(CallbackInfo ci, @Local FluidState fluidState, @Share("fluidPairs") LocalRef<Map<FluidType, FluidPair>> fluidPairsRef) {
        FluidPair fluidPair = ((FluidAccessor) fluidState.getType()).subtleEffects$getFluidPair();
        FluidType type = fluidState.getFluidType();
        Map<FluidType, FluidPair> map = fluidPairsRef.get();
        if (map == null) {
            map = new HashMap<>();
        }

        if (!map.containsKey(type) && fluidPair != null) {
            map.put(type, fluidPair);
        }

        fluidPairsRef.set(map);
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing()V", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;forEach(Ljava/util/function/BiConsumer;)V", remap = false))
    private void updateFluidPairHeight(CallbackInfo ci, @Local Object2ObjectMap<FluidType, Object> interimCalcs, @Share("fluidPairs") LocalRef<Map<FluidType, FluidPair>> fluidPairsRef) {
        interimCalcs.forEach((type, interim) -> {
            FluidPair fluidPair = fluidPairsRef.get().get(type);

            if (fluidPair != null) {
                subtleEffects$fluidPairHeight.put(fluidPair, ((InterimCalculationAccessor) interim).subtleEffects$getFluidHeight());
            }
        });
    }

    @Override
    public double subtleEffects$getFluidHeight(FluidPair fluidPair) {
        return subtleEffects$fluidPairHeight.getDouble(fluidPair);
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

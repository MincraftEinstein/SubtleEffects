package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.util.CommonEntityAccessor;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.FluidHeightAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class FabricClientEntityMixin implements FluidHeightAccessor {

    @Shadow
    protected boolean firstTick;

    @Unique
    private final Object2DoubleMap<FluidPair> subtleEffects$fluidPairHeight = new Object2DoubleArrayMap<>();

    @WrapOperation(method = "updateInWaterStateAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z"))
    private boolean doLavaSplash(Entity entity, TagKey<Fluid> fluidTag, double motionScale, Operation<Boolean> original) {
        subtleEffects$fluidPairHeight.forEach((fluidPair, height) -> {
            boolean b = height > 0;
            // spawn splash

        });

        subtleEffects$fluidPairHeight.clear();
        boolean result = original.call(entity, fluidTag, motionScale);
        boolean isInLava = result && fluidTag == FluidTags.LAVA; // Just in case someone decides to do their own fluid pushing here
        CommonEntityAccessor accessor = (CommonEntityAccessor) entity;
        ParticleSpawnUtil.spawnLavaSplash(entity, isInLava, firstTick, accessor.subtleEffects$wasTouchingLava());
        accessor.subtleEffects$setTouchingLava(isInLava);
        return result;
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private void getFluidPair(TagKey<Fluid> fluidTag, double motionScale, CallbackInfoReturnable<Boolean> cir, @Local FluidState fluidState, @Share("fluidPair") LocalRef<FluidPair> fluidPairRef) {
        FluidPair fluidPair = ((FluidAccessor) fluidState.getType()).subtleEffects$getFluidPair();
        if (fluidPair != null) {
            fluidPairRef.set(fluidPair);
        }
    }

    @WrapOperation(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2DoubleMap;put(Ljava/lang/Object;D)D", remap = false))
    private double updateFluidPairHeight(Object2DoubleMap<TagKey<Fluid>> vanillaFluidHeight, Object fluidTag, double fluidHeight, Operation<Double> original, @Share("fluidPair") LocalRef<FluidPair> fluidPairRef) {
        FluidPair fluidPair = fluidPairRef.get();
        if (fluidPair != null) {
            subtleEffects$fluidPairHeight.put(fluidPair, fluidHeight);
        }

        return original.call(vanillaFluidHeight, fluidTag, fluidHeight);
    }

    @Override
    public double subtleEffects$getFluidHeight(FluidPair fluidPair) {
        return subtleEffects$fluidPairHeight.getDouble(fluidPair);
    }
}

package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.FluidHeightAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class FabricClientEntityMixin implements FluidHeightAccessor {

    @Shadow
    public abstract Level level();

    @Unique
    private final Object2DoubleMap<FluidPair> subtleEffects$fluidPairHeight = new Object2DoubleArrayMap<>();

    @Inject(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private void storeFluidPairHeight(TagKey<Fluid> fluidTag, double motionScale, CallbackInfoReturnable<Boolean> cir, @Local FluidState fluidState, @Share("fluidPair") LocalRef<FluidPair> fluidPairRef) {
        if (level().isClientSide) {
            FluidPair fluidPair = ((FluidAccessor) fluidState.getType()).subtleEffects$getFluidPair();
            if (fluidPair != null) {
                fluidPairRef.set(fluidPair);
            }
        }
    }

    @WrapOperation(method = "updateFluidHeightAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2DoubleMap;put(Ljava/lang/Object;D)D", remap = false))
    private double updateFluidPairHeight(Object2DoubleMap<TagKey<Fluid>> vanillaFluidHeight, Object fluidTag, double fluidHeight, Operation<Double> original, @Share("fluidPair") LocalRef<FluidPair> fluidPairRef) {
        if (level().isClientSide) {
            FluidPair fluidPair = fluidPairRef.get();
            if (fluidPair != null) {
                subtleEffects$fluidPairHeight.put(fluidPair, fluidHeight);
            }
        }

        return original.call(vanillaFluidHeight, fluidTag, fluidHeight);
    }

    @Override
    public Object2DoubleMap<FluidPair> subtleEffects$getFluidPairHeight() {
        return subtleEffects$fluidPairHeight;
    }
}

package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import einstein.subtle_effects.util.FluidAccessor;
import einstein.subtle_effects.util.FluidHeightAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.resources.ResourceLocation;
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

    @Unique
    private final Entity entity = (Entity) (Object) this;

    @Unique
    private FluidPair lastTouchedFluid;

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("HEAD"))
    private void clearFluidPairHeight(CallbackInfoReturnable<Boolean> cir) {
        subtleEffects$fluidPairHeight.clear();
    }

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("TAIL"))
    private void spawnSplash(CallbackInfoReturnable<Boolean> cir) {
        if (!entity.level().isClientSide) {
            return;
        }

        FluidState fluidState = entity.level().getFluidState(entity.blockPosition());
        FluidPair fluidPair = ((FluidAccessor) fluidState.getType()).subtleEffects$getFluidPair();

        if (fluidPair != null) {
            double fluidPairHeight = subtleEffects$fluidPairHeight.getDouble(fluidPair);

            if (fluidPairHeight > 0) {
                if (lastTouchedFluid != fluidPair && !firstTick) {
                    ResourceLocation type = SplashTypeReloadListener.FLUID_PAIR_TO_ID.get(fluidPair);

                    if (type != null) {
                        ParticleSpawnUtil.spawnSplashEffects(entity, entity.level(), type, entity.getY() + fluidPairHeight, entity.getDeltaMovement().y());
                    }
                }
            }
        }
        lastTouchedFluid = fluidPair;
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

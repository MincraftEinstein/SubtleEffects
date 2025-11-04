package einstein.subtle_effects.util;

import einstein.subtle_effects.data.FluidPair;

public interface FluidHeightAccessor {

    double subtleEffects$getFluidHeight(FluidPair fluidPair);

    default boolean isInFluidPair(FluidPair fluidPair) {
        return subtleEffects$getFluidHeight(fluidPair) > 0;
    }
}

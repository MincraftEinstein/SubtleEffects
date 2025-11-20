package einstein.subtle_effects.util;

import einstein.subtle_effects.data.FluidPair;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import org.jetbrains.annotations.Nullable;

public interface FluidHeightAccessor {

    Object2DoubleMap<FluidPair> subtleEffects$getFluidPairHeight();

    @Nullable
    FluidPair subtleEffects$getLastTouchedFluid();

    void subtleEffects$setLastTouchedFluid(@Nullable FluidPair fluidPair);

    void subtleEffects$cancelNextWaterSplash();
}

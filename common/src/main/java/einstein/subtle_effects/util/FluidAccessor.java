package einstein.subtle_effects.util;

import einstein.subtle_effects.data.FluidPair;
import org.jetbrains.annotations.Nullable;

public interface FluidAccessor {

    @Nullable
    FluidPair subtleEffects$getFluidPair();

    void subtleEffects$setFluidPair(@Nullable FluidPair fluidPair);
}

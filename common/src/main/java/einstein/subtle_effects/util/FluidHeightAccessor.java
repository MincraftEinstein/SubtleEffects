package einstein.subtle_effects.util;

import einstein.subtle_effects.data.FluidDefinition;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import org.jetbrains.annotations.Nullable;

public interface FluidHeightAccessor {

    Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight();

    @Nullable
    FluidDefinition subtleEffects$getLastTouchedFluid();

    void subtleEffects$setLastTouchedFluid(@Nullable FluidDefinition fluidDefinition);

    void subtleEffects$cancelNextWaterSplash();
}

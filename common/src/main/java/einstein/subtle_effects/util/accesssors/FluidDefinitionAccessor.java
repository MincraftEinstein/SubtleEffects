package einstein.subtle_effects.util.accesssors;

import einstein.subtle_effects.data.FluidDefinition;
import org.jetbrains.annotations.Nullable;

public interface FluidDefinitionAccessor {

    @Nullable
    FluidDefinition subtleEffects$getFluidDefinition();

    void subtleEffects$setFluidDefinition(@Nullable FluidDefinition fluidDefinition);
}

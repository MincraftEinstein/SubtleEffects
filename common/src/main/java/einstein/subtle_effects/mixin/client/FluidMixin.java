package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Fluid.class)
public class FluidMixin implements FluidDefinitionAccessor {

    @Unique
    private FluidDefinition subtleEffects$fluidDefinition;

    @Override
    public FluidDefinition subtleEffects$getFluidDefinition() {
        return subtleEffects$fluidDefinition;
    }

    @Override
    public void subtleEffects$setFluidDefinition(FluidDefinition fluidDefinition) {
        subtleEffects$fluidDefinition = fluidDefinition;
    }
}

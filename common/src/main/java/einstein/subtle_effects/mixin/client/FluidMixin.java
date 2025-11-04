package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.FluidPair;
import einstein.subtle_effects.util.FluidAccessor;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Fluid.class)
public class FluidMixin implements FluidAccessor {

    @Unique
    private FluidPair subtleEffects$fluidPair;

    @Override
    public FluidPair subtleEffects$getFluidPair() {
        return subtleEffects$fluidPair;
    }

    @Override
    public void subtleEffects$setFluidPair(FluidPair fluidPair) {
        subtleEffects$fluidPair = fluidPair;
    }
}

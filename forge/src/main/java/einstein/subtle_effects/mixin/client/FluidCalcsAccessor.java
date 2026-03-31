package einstein.subtle_effects.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.entity.Entity$FluidCalcs", remap = false)
public interface FluidCalcsAccessor {

    @Accessor("height")
    double getHeight();
}

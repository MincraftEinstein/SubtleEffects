package einstein.subtle_effects.mixin.client.particle;

import net.minecraft.core.particles.ColorParticleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ColorParticleOption.class)
public interface ColorParticleOptionAccessor {

    @Accessor("color")
    int getColor();
}

package einstein.subtle_effects.mixin.client.particle;

import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SingleQuadParticle.class)
public interface SingleQuadParticleAccessor {

    @Accessor("quadSize")
    void setQuadSize(float quadSize);
}

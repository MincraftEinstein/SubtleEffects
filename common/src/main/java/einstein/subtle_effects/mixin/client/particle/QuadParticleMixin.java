package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.util.QuadParticleAccessor;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SingleQuadParticle.class)
public abstract class QuadParticleMixin implements QuadParticleAccessor {

    @Override
    @Accessor("alpha")
    public abstract float getAlpha();

    @Override
    @Accessor("alpha")
    public abstract void setAlpha(float alpha);
}

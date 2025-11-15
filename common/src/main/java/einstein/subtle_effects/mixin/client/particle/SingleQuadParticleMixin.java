package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.util.SingleQuadParticleAccessor;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SingleQuadParticle.class)
public abstract class SingleQuadParticleMixin implements SingleQuadParticleAccessor {

    @Override
    @Accessor("alpha")
    public abstract float getAlpha();

    @Override
    @Accessor("alpha")
    public abstract void setAlpha(float alpha);

    @Override
    @Invoker("getLayer")
    public abstract SingleQuadParticle.Layer subtleEffects$getLayer();
}

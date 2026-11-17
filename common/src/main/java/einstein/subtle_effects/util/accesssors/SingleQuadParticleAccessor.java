package einstein.subtle_effects.util.accesssors;

import net.minecraft.client.particle.SingleQuadParticle;

public interface SingleQuadParticleAccessor {

    float getAlpha();

    void setAlpha(float alpha);

    SingleQuadParticle.Layer subtleEffects$getLayer();
}

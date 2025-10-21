package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Particle.class)
public abstract class ParticleMixin implements ParticleAccessor {

    @Unique
    private boolean subtleEffects$forced = false;

    @Unique
    private boolean subtleEffects$ignoreCulling = false;

    @Override
    @Accessor("x")
    public abstract double getX();

    @Override
    @Accessor("y")
    public abstract double getY();

    @Override
    @Accessor("z")
    public abstract double getZ();

    @Override
    @Accessor("alpha")
    public abstract float getAlpha();

    @Override
    @Accessor("alpha")
    public abstract void setAlpha(float alpha);

    @Override
    @Accessor("gravity")
    public abstract void setGravity(float gravity);

    @Override
    @Accessor("hasPhysics")
    public abstract void setHasPhysics(boolean hasPhysics);

    @Override
    @Accessor("bbWidth")
    public abstract float getWidth();

    @Override
    @Accessor("bbHeight")
    public abstract float getHeight();

    @Override
    @Invoker("setSize")
    public abstract void setSizes(float width, float height);

    @Override
    public boolean subtleEffects$wasForced() {
        return subtleEffects$forced;
    }

    @Override
    public void subtleEffects$force() {
        subtleEffects$forced = true;
    }

    @Override
    public boolean subtleEffects$shouldIgnoreCulling() {
        return subtleEffects$ignoreCulling;
    }

    @Override
    public void subtleEffects$ignoresCulling() {
        subtleEffects$ignoreCulling = true;
    }
}

package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public abstract class ParticleMixin implements ParticleAccessor {

    @Shadow
    protected float gravity;
    @Unique
    private boolean subtleEffects$forced = false;

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
    public boolean subtleEffects$wasForced() {
        return subtleEffects$forced;
    }

    @Override
    public void subtleEffects$force() {
        subtleEffects$forced = true;
    }

    @Override
    public float getGravity() {
        return gravity;
    }
}

package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.util.ParticleAccessor;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public abstract class ParticleMixin implements ParticleAccessor {

    @Unique
    private boolean ambientSleep$forced = false;

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
    public boolean ambientSleep$wasForced() {
        return ambientSleep$forced;
    }

    @Override
    public void ambientSleep$force() {
        ambientSleep$forced = true;
    }
}

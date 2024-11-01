package einstein.subtle_effects.particle.emitter;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;

public abstract class AbstractParticleEmitter extends NoRenderParticle {

    private final double size;
    private final int tickDelay;
    private final int count;

    protected AbstractParticleEmitter(ClientLevel level, double x, double y, double z, int lifetime, double size, int tickDelay, int count) {
        super(level, x, y, z);
        this.lifetime = lifetime;
        this.size = size;
        this.tickDelay = tickDelay;
        this.count = count;
    }

    @Override
    public void tick() {
        if (age % (tickDelay + 1) == 0) {
            for (int i = 0; i < count; i++) {
                spawnParticle(
                        x + (random.nextDouble() - random.nextDouble()) * size,
                        y + (random.nextDouble() - random.nextDouble()) * size,
                        z + (random.nextDouble() - random.nextDouble()) * size
                );
            }
        }

        if (age++ >= lifetime) {
            remove();
        }
    }

    protected abstract void spawnParticle(double x, double y, double z);
}

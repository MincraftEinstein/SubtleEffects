package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.monster.Slime;

import java.util.function.Supplier;

public class SlimeTrailTicker<T extends Slime> extends EntityTicker<T> {

    private final Supplier<ParticleType<FloatParticleOptions>> type;
    private boolean wasInAir;

    public SlimeTrailTicker(T entity, Supplier<ParticleType<FloatParticleOptions>> type) {
        super(entity);
        this.type = type;
    }

    @Override
    public void entityTick() {
        if (wasInAir && entity.onGround()) {
            level.addParticle(new FloatParticleOptions(type.get(), entity.getSize() * 0.5F),
                    entity.getX(),
                    entity.getBlockY() + (random.nextDouble() / 10),
                    entity.getZ(),
                    0, 0, 0
            );
        }
        wasInAir = !entity.onGround();
    }
}

package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import net.minecraft.world.entity.monster.Slime;

public class SlimeTrailTicker extends Ticker<Slime> {

    private boolean wasInAir;

    public SlimeTrailTicker(Slime entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (wasInAir && entity.onGround()) {
            level.addParticle(new FloatParticleOptions(ModParticles.SLIME_TRAIL.get(), entity.getSize() * 0.5F),
                    entity.getX(),
                    entity.getBlockY() + (random.nextDouble() / 10),
                    entity.getZ(),
                    0, 0, 0
            );
        }
        wasInAir = !entity.onGround();
    }
}

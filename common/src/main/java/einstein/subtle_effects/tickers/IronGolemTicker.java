package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemTicker extends Ticker<IronGolem> {

    private IronGolem.Crackiness oldCrackinessLevel;
    private float oldHealth;

    public IronGolemTicker(IronGolem entity) {
        super(entity);
    }

    @Override
    public void tick() {
        IronGolem.Crackiness crackinessLevel = entity.getCrackiness();
        if (oldCrackinessLevel != crackinessLevel) {
            oldCrackinessLevel = crackinessLevel;

            float health = entity.getHealth();
            if (oldHealth != health) {
                if (health < oldHealth) {
                    for (int i = 0; i < 20; i++) {
                        level.addParticle(ModParticles.IRON_GOLEM.get(),
                                entity.getRandomX(0.5),
                                entity.getRandomY(),
                                entity.getRandomZ(0.5),
                                MathUtil.nextNonAbsDouble(random) / 10,
                                MathUtil.nextNonAbsDouble(random) / 10,
                                MathUtil.nextNonAbsDouble(random) / 10
                        );
                    }
                }

                oldHealth = health;
            }
        }
    }
}

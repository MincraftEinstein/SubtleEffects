package einstein.subtle_effects.tickers;

import einstein.subtle_effects.util.MathUtil;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class IronGolemTicker extends Ticker<IronGolem> {

    private Crackiness.Level oldCrackinessLevel;
    private float oldHealth;

    public IronGolemTicker(IronGolem entity) {
        super(entity);
    }

    @Override
    public void tick() {
        Crackiness.Level crackinessLevel = entity.getCrackiness();
        if (oldCrackinessLevel != crackinessLevel) {
            oldCrackinessLevel = crackinessLevel;

            float health = entity.getHealth();
            if (oldHealth != health) {
                if (health < oldHealth) {
                    for (int i = 0; i < 20; i++) {
                        level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.IRON_INGOT)),
                                entity.getRandomX(0.5),
                                entity.getRandomY(),
                                entity.getRandomZ(0.5),
                                MathUtil.nextNonAbsDouble() / 10,
                                MathUtil.nextNonAbsDouble() / 10,
                                MathUtil.nextNonAbsDouble() / 10
                        );
                    }
                }

                oldHealth = health;
            }
        }
    }
}

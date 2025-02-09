package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.ItemStack;

public class WitchTicker extends Ticker<Witch> {

    private boolean wasDrinkingPotion = false;

    public WitchTicker(Witch entity) {
        super(entity);
    }

    @Override
    public void tick() {
        boolean isDrinkingPotion = entity.isDrinkingPotion();
        if (wasDrinkingPotion != isDrinkingPotion) {
            ItemStack stack = entity.getMainHandItem();

            if (!stack.isEmpty() && stack.has(DataComponents.POTION_CONTENTS)) {
                // noinspection all
                int color = stack.get(DataComponents.POTION_CONTENTS).getColor();

                level.addParticle(new ColorAndIntegerParticleOptions(ModParticles.POTION_EMITTER.get(), color, entity.getId()),
                        entity.getX(), entity.getY(), entity.getZ(),
                        0, 0, 0);
            }
        }
        wasDrinkingPotion = isDrinkingPotion;
    }
}

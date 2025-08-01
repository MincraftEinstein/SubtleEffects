package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;

public class HumanoidPotionRingTicker<T extends LivingEntity> extends EntityTicker<T> {

    protected boolean wasUsingItem = false;

    public HumanoidPotionRingTicker(T entity) {
        super(entity, true);
    }

    @Override
    public void entityTick() {
        boolean isUsingItem = isUsingItem();
        if (wasUsingItem != isUsingItem) {
            ItemStack stack = entity.getMainHandItem();

            if (!stack.isEmpty() && stack.getItem() instanceof PotionItem) {
                // noinspection all
                if (!PotionUtils.getMobEffects(stack).isEmpty()) {
                    int color = PotionUtils.getColor(stack);

                    level.addParticle(new ColorAndIntegerParticleOptions(ModParticles.POTION_EMITTER.get(), color, entity.getId()),
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            0, 0, 0
                    );
                }
            }
        }
        wasUsingItem = isUsingItem;
    }

    protected boolean isUsingItem() {
        return entity.isUsingItem();
    }
}

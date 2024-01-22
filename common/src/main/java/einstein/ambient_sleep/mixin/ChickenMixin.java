package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chicken.class)
public abstract class ChickenMixin extends Animal {

    protected ChickenMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide && source.getEntity() instanceof LivingEntity && isAlive() && hurtTime == 0) {
            for (int i = 0; i < 10; i++) {
                level().addParticle(ModParticles.CHICKEN_FEATHER.get(), getX(), getY(), getZ(), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
            }
        }
        return super.hurt(source, amount);
    }
}

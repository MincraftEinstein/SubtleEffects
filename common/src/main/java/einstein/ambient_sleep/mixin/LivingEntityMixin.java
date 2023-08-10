package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.init.ModInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract boolean isSleeping();

    @Unique
    private int ambientSleep$breatheTimer = 0;

    @Unique
    private int ambientSleep$snoreTimer = 0;

    @Unique
    private int ambientSleep$snoreCount = 0;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (level().isClientSide) {
            if (isSleeping()) {
                if (ambientSleep$breatheTimer < AmbientSleep.BREATH_DELAY) {
                    ambientSleep$breatheTimer++;
                }
                else {
                    if (ambientSleep$snoreTimer >= AmbientSleep.SNORE_DELAY) {
                        ambientSleep$snoreTimer = 0;
                        ambientSleep$snoreCount++;
                        level().addParticle(ModInit.SNORING_PARTICLE.get(), getX(), getY() + 0.5, getZ(), 0, 0, 0);

                        if (ambientSleep$snoreCount >= 3) {
                            ambientSleep$snoreCount = 0;
                            ambientSleep$breatheTimer = 0;
                        }
                    }

                    if (ambientSleep$snoreTimer < AmbientSleep.SNORE_DELAY) {
                        ambientSleep$snoreTimer++;
                    }
                }
            }
            else {
                ambientSleep$breatheTimer = 0;
                ambientSleep$snoreTimer = 0;
                ambientSleep$snoreCount = 0;
            }
        }
    }
}
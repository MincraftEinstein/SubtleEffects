package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin<T extends Entity> extends Entity {

    @Shadow
    public int deathTime;

    public ClientLivingEntityMixin(EntityType<T> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tickDeath", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;deathTime:I", ordinal = 0))
    private void deathTick(CallbackInfo ci) {
        if (!level().isClientSide) {
            return;
        }

        if (ModConfigs.ENTITIES.wardenDeathSoulParticles && getType().equals(EntityType.WARDEN)) {
            if (deathTime == 1 && !isRemoved()) {
                for (int i = 0; i < random.nextIntBetweenInclusive(3, 5); i++) {
                    level().addParticle(ParticleTypes.SCULK_SOUL,
                            position().x() + (getBbWidth() / 2) + MathUtil.nextNonAbsDouble(random),
                            position().y() + (getBbHeight() / 2) + MathUtil.nextNonAbsDouble(random),
                            position().z() + (getBbWidth() / 2) + MathUtil.nextNonAbsDouble(random),
                            0,
                            Mth.nextFloat(getRandom(), 0.01F, 0.1F),
                            0
                    );
                }
            }
        }
    }
}
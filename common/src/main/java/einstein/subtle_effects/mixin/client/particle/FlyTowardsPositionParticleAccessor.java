package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.QuadParticleAccessor;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlyTowardsPositionParticle.class)
public interface FlyTowardsPositionParticleAccessor {

    @Mutable
    @Accessor("isGlowing")
    void setGlowing(boolean glowing);

    @Mutable
    @Accessor("lifetimeAlpha")
    void setLifetimeAlpha(Particle.LifetimeAlpha lifetimeAlpha);

    @Mixin(FlyTowardsPositionParticle.EnchantProvider.class)
    class EnchantProviderMixin {

        @ModifyReturnValue(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
        private Particle init(Particle particle) {
            FlyTowardsPositionParticleAccessor accessor = (FlyTowardsPositionParticleAccessor) particle;
            if (ModConfigs.GENERAL.glowingEnchantmentParticles) {
                accessor.setGlowing(true);
            }

            if (ModConfigs.GENERAL.translucentEnchantmentParticles) {
                accessor.setLifetimeAlpha(new Particle.LifetimeAlpha(0, 0.5F, 0, 1));
                ((QuadParticleAccessor) particle).setAlpha(0.5F);
            }

            if (ModConfigs.GENERAL.disableRandomizedShading) {
                if (particle instanceof SingleQuadParticle sqParticle) {
                    sqParticle.setColor(1, 1, 1);
                }
            }
            return particle;
        }
    }
}

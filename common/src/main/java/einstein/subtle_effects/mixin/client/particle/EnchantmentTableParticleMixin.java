package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.EnchantmentTableParticleAccessor;
import einstein.subtle_effects.util.LifetimeAlpha;
import einstein.subtle_effects.util.ParticleAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.EnchantmentTableParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentTableParticle.class)
public abstract class EnchantmentTableParticleMixin extends TextureSheetParticle implements EnchantmentTableParticleAccessor {

    @Unique
    private LifetimeAlpha subtleEffects$lifetimeAlpha;

    @Unique
    private boolean subtleEffects$glowing = false;

    protected EnchantmentTableParticleMixin(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @ModifyReturnValue(method = "getLightColor", at = @At("RETURN"))
    private int getLightColor(int lightColor) {
        return subtleEffects$glowing ? 240 : lightColor;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (subtleEffects$lifetimeAlpha != null) {
            subtleEffects$lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
        }
        super.render(consumer, camera, partialTicks);
    }

    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    private ParticleRenderType getRenderType(ParticleRenderType original) {
        if (subtleEffects$lifetimeAlpha != null) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        return original;
    }

    @Override
    public void subtleEffects$setLifetimeAlpha(LifetimeAlpha alpha) {
        subtleEffects$lifetimeAlpha = alpha;
    }

    @Override
    public void subtleEffects$setGlowing(boolean glowing) {
        this.subtleEffects$glowing = glowing;
    }

    @Mixin(EnchantmentTableParticle.Provider.class)
    public static class ProviderMixin {

        @ModifyReturnValue(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
        private Particle init(Particle particle) {
            EnchantmentTableParticleAccessor accessor = (EnchantmentTableParticleAccessor) particle;
            if (ModConfigs.GENERAL.glowingEnchantmentParticles) {
                accessor.subtleEffects$setGlowing(true);
            }

            if (ModConfigs.GENERAL.translucentEnchantmentParticles) {
                accessor.subtleEffects$setLifetimeAlpha(new LifetimeAlpha(0, 0.5F, 0, 1));
                ((ParticleAccessor) particle).setAlpha(0.5F);
            }

            if (ModConfigs.GENERAL.disableRandomizedShading) {
                particle.setColor(1, 1, 1);
            }
            return particle;
        }
    }
}

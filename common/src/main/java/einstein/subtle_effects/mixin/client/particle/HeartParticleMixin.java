package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.HeartParticleAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeartParticle.class)
public abstract class HeartParticleMixin extends SingleQuadParticle implements HeartParticleAccessor {

    @Unique
    private boolean subtleEffects$isHeart;

    protected HeartParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, CallbackInfo ci) {
        lifetime = random.nextIntBetweenInclusive(16, 25);
    }

    @Override
    public void tick() {
        super.tick();

        if (subtleEffects$isHeart) {
            if (ModConfigs.GENERAL.poppingHearts && !isAlive()) {
                level.addParticle(ModParticles.HEART_POP.get(), x, y, z, 0, yd, 0);
            }
        }
    }

    @Override
    public void subtleEffects$setHeart() {
        subtleEffects$isHeart = true;
    }

    @Mixin(HeartParticle.Provider.class)
    public static class ProviderMixin {

        @ModifyReturnValue(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
        private Particle setAsHeart(Particle original, @Local(argsOnly = true) SimpleParticleType type) {
            if (type.equals(ParticleTypes.HEART)) {
                ((HeartParticleAccessor) original).subtleEffects$setHeart();
            }
            return original;
        }
    }
}

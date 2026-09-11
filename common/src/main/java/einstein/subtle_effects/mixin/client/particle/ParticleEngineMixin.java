package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.BCWPPackManager;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow
    protected ClientLevel level;

    @WrapOperation(method = "createParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;makeParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;"))
    private static <T extends ParticleOptions> Particle replaceParticle(ParticleEngine engine, T particleOptions, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Particle> original) {
        if (GENERAL.growingHearts) {
            if (particleOptions.getType() == ParticleTypes.HEART) {
                return original.call(engine, ModParticles.HEART_GROWTH.get(), x, y, z, xSpeed, ySpeed, zSpeed);
            }
        }
        return original.call(engine, particleOptions, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Inject(method = "createParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private void modifyParticle(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir, @Local Particle particle) {
        ParticleType<?> type = options.getType();
        if (GENERAL.particleCullingBlocklist.get().contains(type)) {
            ((ParticleAccessor) particle).subtleEffects$ignoresCulling();
        }

        if (BCWPPackManager.isPackLoaded() && BCWPPackManager.BIOME_COLORED_PARTICLES.contains(type)) {
            Util.setColorFromHex(particle, BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z)));
        }
    }
}

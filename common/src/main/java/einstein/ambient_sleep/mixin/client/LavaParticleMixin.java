package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.client.particle.LavaParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LavaParticle.class)
public class LavaParticleMixin {

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke() {
        if (ModConfigs.INSTANCE.lavaSparkSmoke.get()) {
            return ModParticles.SMOKE.get();
        }
        return ParticleTypes.SMOKE;
    }
}

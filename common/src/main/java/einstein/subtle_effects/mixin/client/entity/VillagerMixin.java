package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.SplashDropletParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.util.MathUtil.*;

@Mixin(Villager.class)
public class VillagerMixin {

    @WrapOperation(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;addParticlesAroundSelf(Lnet/minecraft/core/particles/ParticleOptions;)V"))
    private void replaceSplashParticles(Villager villager, ParticleOptions options, Operation<Void> original) {
        if (true && options.getType() == ParticleTypes.SPLASH) {
            RandomSource random = villager.getRandom();
            Level level = villager.level();
            Vec3 pos = villager.position();

            for (int i = 0; i < 10; i++) {
                int xSign = nextSign(random);
                int zSign = nextSign(random);

                level.addParticle(new SplashDropletParticleOptions(ModParticles.WATER_SPLASH_DROPLET.get(), 1),
                        pos.x() + nextDouble(random, 0.2) * xSign,
                        pos.y() + villager.getEyeHeight() + nextNonAbsDouble(random, 0.3),
                        pos.z() + nextDouble(random, 0.2) * zSign,
                        nextDouble(random, 0.15) * xSign,
                        nextDouble(random, 0.35),
                        nextDouble(random, 0.15) * zSign
                );
            }
            return;
        }
        original.call(villager, options);
    }
}

package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(MushroomCow.class)
public class MooshroomMixin {

    @Unique
    private final MushroomCow subtleEffects$me = (MushroomCow) (Object) this;

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private void spawnFeedingFailedParticles(Level level, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ModConfigs.ENTITIES.improvedBrownMooshroomFeedingEffects) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(particle,
                        subtleEffects$me.getRandomX(1),
                        subtleEffects$me.getRandomY(),
                        subtleEffects$me.getRandomZ(1),
                        0, 0, 0
                );
            }
            return;
        }
        original.call(level, particle, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private void spawnFeedingParticles(Level level, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original, @Local Optional<SuspiciousStewEffects> optional, @Local(ordinal = 0) ItemStack heldStack) {
        RandomSource random = subtleEffects$me.getRandom();

        if (ModConfigs.ENTITIES.animalFeedingParticles) {
            for (int i = 0; i < 4; i++) {
                ParticleSpawnUtil.spawnEntityFaceParticle(new ItemParticleOption(ParticleTypes.ITEM, heldStack),
                        subtleEffects$me, level, random, new Vec3(0, 0.3, -0.2),
                        Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)
                );
            }
        }

        if (ModConfigs.ENTITIES.improvedBrownMooshroomFeedingEffects) {
            List<SuspiciousStewEffects.Entry> effects = optional.get().effects();
            int color = effects.get(random.nextInt(effects.size())).effect().value().getColor();

            for (int i = 0; i < 2; i++) {
                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color),
                        subtleEffects$me.getRandomX(1),
                        subtleEffects$me.getRandomY(),
                        subtleEffects$me.getRandomZ(1),
                        0, 0, 0
                );
            }
            return;
        }

        original.call(level, particle, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}

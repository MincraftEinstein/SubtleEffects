package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

@Mixin(MushroomCow.class)
public class MooshroomMixin {

    @Unique
    private final MushroomCow subtleEffects$me = (MushroomCow) (Object) this;

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private void spawnFeedingFailedParticles(Level level, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (ENTITIES.improvedBrownMooshroomFeedingEffects) {
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

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/tuple/Pair;getLeft()Ljava/lang/Object;"))
    private void spawnFeedingParticles(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local Optional<Pair<MobEffect, Integer>> optional, @Local(ordinal = 0) ItemStack heldStack) {
        Level level = subtleEffects$me.level();
        RandomSource random = subtleEffects$me.getRandom();

        if (ENTITIES.animalFeedingParticles && !heldStack.isEmpty()) {
            ItemParticleOption options = new ItemParticleOption(ParticleTypes.ITEM, heldStack);

            for (int i = 0; i < 16; i++) {
                ParticleSpawnUtil.spawnEntityFaceParticle(options, subtleEffects$me, level,
                        random, new Vec3(0, 0.3, -0.2), Util.getPartialTicks()
                );
            }
        }

        if (ModConfigs.ENTITIES.improvedBrownMooshroomFeedingEffects) {
            int color = optional.get().getLeft().getColor();
            Vec3 colorVec = Vec3.fromRGB24(color);

            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.ENTITY_EFFECT,
                        subtleEffects$me.getRandomX(1),
                        subtleEffects$me.getRandomY(),
                        subtleEffects$me.getRandomZ(1),
                        colorVec.x, colorVec.y, colorVec.z
                );
            }
        }
    }

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private void spawnEffectParticles(Level level, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (!ENTITIES.improvedBrownMooshroomFeedingEffects) {
            original.call(level, particle, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}

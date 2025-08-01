package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Panda.class)
public abstract class PandaMixin extends Animal {

    @Unique
    private final Panda subtleEffects$panda = (Panda) (Object) this;

    protected PandaMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Inject into the mobInteract method to detect right-clicking on entity
    @ModifyReturnValue(method = "mobInteract", at = @At("RETURN"))
    private InteractionResult onPandaInteract(InteractionResult result, @Local(argsOnly = true) Player player, @Local(argsOnly = true) InteractionHand hand) {
        Level level = subtleEffects$panda.level();
        if (level.isClientSide()) {
            if (result != InteractionResult.PASS
                    || !ModConfigs.ENTITIES.featherTicklingPandas
                    || subtleEffects$panda.isSneezing()
                    || !subtleEffects$panda.canPerformAction()
            ) {
                return result;
            }

            boolean isBaby = subtleEffects$panda.isBaby();

            // Check if panda is a baby or weak
            if (isBaby || subtleEffects$panda.isWeak()) {

                // Check if player is holding a feather and can be tickled
                if (player.getItemInHand(hand).is(Items.FEATHER)) {
                    SoundSource soundSource = getSoundSource();
                    float pitch = isBaby ? 1 : 0.7F;

                    player.swing(hand);
                    subtleEffects$panda.sneeze(true);
                    Util.playClientSound(subtleEffects$panda, SoundEvents.PANDA_PRE_SNEEZE,
                            soundSource, 1, pitch
                    );

                    TickerManager.schedule(20, () -> {
                        Util.playClientSound(subtleEffects$panda, SoundEvents.PANDA_SNEEZE,
                                soundSource, 1, pitch
                        );

                        if (ModConfigs.ENTITIES.improvedPandaSneezes) {
                            subtleEffects$spawnSneezeParticles(level, isBaby);
                            return;
                        }

                        Vec3 deltaMovement = subtleEffects$panda.getDeltaMovement();
                        double offset = (getBbWidth() + 1) * (isBaby ? 0.5 : 0.7);
                        float rotation = yBodyRot * Mth.DEG_TO_RAD;

                        level.addParticle(ParticleTypes.SNEEZE,
                                getX() - offset * Mth.sin(rotation),
                                getEyeY() - (isBaby ? 0.1 : 0.5),
                                getZ() + offset * Mth.cos(rotation),
                                deltaMovement.x, 0, deltaMovement.z
                        );
                    });
                }
            }
        }
        return result;
    }

    @WrapOperation(method = "afterSneeze", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void replaceSneezeParticles(Level level, ParticleOptions particle, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (level.isClientSide && ModConfigs.ENTITIES.improvedPandaSneezes) {
            subtleEffects$spawnSneezeParticles(level, subtleEffects$panda.isBaby());
            return;
        }
        original.call(level, particle, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Unique
    private void subtleEffects$spawnSneezeParticles(Level level, boolean isBaby) {
        Vec3 offset = isBaby ? new Vec3(-0.03, -0.33, 0) : new Vec3(0, -0.35, 0.9);
        for (int i = 0; i < 16; i++) {
            ParticleSpawnUtil.spawnEntityFaceParticle(ModParticles.SNEEZE.get(),
                    subtleEffects$panda, level, random, offset,
                    new Vec3(MathUtil.nextNonAbsDouble(random, 0.02), 0, Mth.nextDouble(random, 0.03, 0.08)),
                    Minecraft.getInstance().getFrameTime()
            );
        }
    }
}

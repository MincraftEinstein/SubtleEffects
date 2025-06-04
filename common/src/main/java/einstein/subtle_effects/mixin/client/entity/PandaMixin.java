package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.Util;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Panda.class)
public abstract class PandaMixin extends Animal {

    @Unique
    private final Panda subtleEffects$panda = (Panda) (Object) this;

    @Unique
    private boolean subtleEffects$canNoseBeTickled = true;

    protected PandaMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Inject into the mobInteract method to detect right-clicking on entity
    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void onPandaInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = subtleEffects$panda.level();
        if (level.isClientSide() && ModConfigs.ENTITIES.pandaFeatherSneeze) {
            boolean isBaby = subtleEffects$panda.isBaby();

            // Check if panda is a baby or weak
            if (isBaby || subtleEffects$panda.isWeak()) {

                // Check if player is holding a feather and can be tickled
                if (player.getItemInHand(hand).is(Items.FEATHER) && subtleEffects$canNoseBeTickled) {
                    SoundSource soundSource = getSoundSource();
                    float pitch = isBaby ? 1 : 0.7F;

                    player.swing(hand);
                    subtleEffects$canNoseBeTickled = false;
                    Util.playClientSound(subtleEffects$panda, SoundEvents.PANDA_PRE_SNEEZE,
                            soundSource, 1, pitch
                    );

                    TickerManager.schedule(100, () -> subtleEffects$canNoseBeTickled = true);
                    TickerManager.schedule(20, () -> {
                        Vec3 deltaMovement = subtleEffects$panda.getDeltaMovement();
                        double offset = (getBbWidth() + 1) * (isBaby ? 0.5 : 0.7);
                        float rotation = yBodyRot * Mth.DEG_TO_RAD;

                        level.addParticle(ParticleTypes.SNEEZE,
                                getX() - offset * Mth.sin(rotation),
                                getEyeY() - (isBaby ? 0.1 : 0.5),
                                getZ() + offset * Mth.cos(rotation),
                                deltaMovement.x, 0, deltaMovement.z
                        );

                        Util.playClientSound(subtleEffects$panda, SoundEvents.PANDA_SNEEZE,
                                soundSource, 1, pitch
                        );
                    });
                }
            }
        }
    }
}

package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public class SnowballMixin {

    @Unique
    private final Snowball subtleEffects$me = (Snowball) (Object) this;

    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void handleEntityEvent(byte event, CallbackInfo ci) {
        if (event == EntityEvent.DEATH) {
            Level level = subtleEffects$me.level();
            RandomSource random = subtleEffects$me.getRandom();

            if (ModConfigs.ENTITIES.snowballPoofsHaveSnowflakes) {
                for (int i = 0; i < 8; i++) {
                    level.addParticle(ModParticles.SNOW.get(),
                            subtleEffects$me.getX(),
                            subtleEffects$me.getY(),
                            subtleEffects$me.getZ(),
                            0, 0, 0
                    );
                }
            }

            float volume = ModConfigs.ENTITIES.snowballPoofSoundVolume.get();
            if (volume > 0) {
                level.playSound(
                        Minecraft.getInstance().player,
                        subtleEffects$me.getX(),
                        subtleEffects$me.getY(),
                        subtleEffects$me.getZ(),
                        ModSounds.SNOWBALL_POOF.get(),
                        subtleEffects$me.getSoundSource(),
                        volume,
                        Mth.nextFloat(random, 0.7F, 1.2F)
                );
            }
        }
    }
}

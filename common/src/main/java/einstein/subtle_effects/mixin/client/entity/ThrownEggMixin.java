package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin {

    @Unique
    private final ThrownEgg subtleEffects$me = (ThrownEgg) (Object) this;

    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void handle(byte id, CallbackInfo ci) {
        if (id == 3) {
            Level level = subtleEffects$me.level();
            RandomSource random = subtleEffects$me.getRandom();
            float volume = ModConfigs.ITEMS.projectiles.eggSmashSoundVolume.get();

            if (volume > 0) {
                level.playSound(
                        Minecraft.getInstance().player,
                        subtleEffects$me.getX(),
                        subtleEffects$me.getY(),
                        subtleEffects$me.getZ(),
                        ModSounds.EGG_BREAK.get(),
                        SoundSource.PLAYERS,
                        volume,
                        Mth.nextFloat(random, 0.7F, 1.5F)
                );
            }

            if (ModConfigs.ITEMS.projectiles.eggSplatParticles) {
                List<Entity> spawnedEntities = level.getEntities((Entity) null, subtleEffects$me.getBoundingBox(), (Entity entity) -> {
                    if (entity instanceof AgeableMob ageableMob) {
                        return ageableMob.isBaby();
                    }
                    return false;
                });

                if (spawnedEntities.isEmpty()) {
                    ParticleSpawnUtil.spawnProjectileSplat(subtleEffects$me, level, random, ModParticles.EGG_SPLAT.get());
                }
            }
        }
    }
}

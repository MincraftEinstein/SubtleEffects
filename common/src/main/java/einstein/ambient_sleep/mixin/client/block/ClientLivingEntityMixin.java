package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.init.ModSounds;
import einstein.ambient_sleep.util.ParticleManager;
import einstein.ambient_sleep.util.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin extends Entity {

    @Shadow
    public abstract boolean isSleeping();

    @Shadow
    public abstract float getVoicePitch();

    @Shadow
    public int hurtTime;

    @Unique
    private int ambientSleep$breatheTimer = 0;

    @Unique
    private int ambientSleep$snoreTimer = 0;

    @Unique
    private int ambientSleep$ZCount = 0;

    @SuppressWarnings("all")
    @Unique
    private final LivingEntity ambientSleep$me = (LivingEntity) (Object) this;

    public ClientLivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (level().isClientSide) {
            double chance = 1;
            SoundEvent snoreSound = null;
            if (ambientSleep$me instanceof Player) {
                chance = ModConfigs.INSTANCE.playerSnoreChance.get();
                snoreSound = ModSounds.PLAYER_SNORE.get();
            }
            else if (ambientSleep$me instanceof Villager) {
                chance = ModConfigs.INSTANCE.villagerSnoreChance.get();
                snoreSound = ModSounds.VILLAGER_SNORE.get();
            }

            if (isSleeping()) {
                if (ambientSleep$breatheTimer < Util.BREATH_DELAY) {
                    ambientSleep$breatheTimer++;
                }
                else {
                    if (ambientSleep$snoreTimer >= Util.SNORE_DELAY) {
                        boolean doesSnore = ambientSleep$doesEntitySnore(ambientSleep$me, chance);
                        if (ambientSleep$ZCount <= 0 && snoreSound != null && doesSnore) {
                            Util.playClientSound(SoundSource.NEUTRAL, ambientSleep$me, snoreSound, 1, getVoicePitch());
                        }

                        ambientSleep$snoreTimer = 0;
                        ambientSleep$ZCount++;
                        if (ModConfigs.INSTANCE.enableSleepingZs.get()) {
                            boolean onlyWhenSnoring = ModConfigs.INSTANCE.displaySleepingZsOnlyWhenSnoring.get();

                            if ((ambientSleep$me instanceof Fox && ModConfigs.INSTANCE.foxesHaveSleepingZs.get()) || (!(ambientSleep$me instanceof Fox) && (!onlyWhenSnoring || doesSnore))) {
                                level().addParticle(ModParticles.SNORING.get(), getX(), getY() + 0.5, getZ(), 0, 0, 0);
                            }
                        }

                        if (ambientSleep$ZCount >= Util.MAX_Z_COUNT) {
                            ambientSleep$ZCount = 0;
                            ambientSleep$breatheTimer = 0;
                        }
                    }

                    if (ambientSleep$snoreTimer < Util.SNORE_DELAY) {
                        ambientSleep$snoreTimer++;
                    }
                }
            }
            else {
                ambientSleep$breatheTimer = 0;
                ambientSleep$snoreTimer = 0;
                ambientSleep$ZCount = 0;
            }
        }
    }

    @Unique
    private static boolean ambientSleep$doesEntitySnore(LivingEntity entity, double chance) {
        UUID uuid = entity.getUUID();
        return Double.parseDouble("0." + Math.abs(uuid.hashCode())) < chance;
    }

    @Redirect(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 0))
    private boolean cancelFlyIntoWallServerCheck(Level level) {
        return false;
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void cancelFlyIntoWallClientSound(LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        if (!level().isClientSide) {
            playSound(sound, volume, pitch);
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean cancelFlyIntoWallClientHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!level().isClientSide) {
            return entity.hurt(source, amount);
        }

        if (entity instanceof Player player && player.isCreative()) {
            return false;
        }

        Util.spawnFallDustClouds(entity, 10, 10);
        return false;
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (level().isClientSide && !isInvulnerableTo(source) && amount > 0) {
            if (source.getEntity() instanceof LivingEntity && isAlive() && hurtTime == 0) {
                ParticleManager.entityHurt(ambientSleep$me, level(), random);
            }
        }
    }
}
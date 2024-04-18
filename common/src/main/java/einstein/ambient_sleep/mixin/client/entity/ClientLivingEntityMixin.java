package einstein.ambient_sleep.mixin.client.entity;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.init.ModSounds;
import einstein.ambient_sleep.util.ParticleManager;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
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
    private SoundEvent ambientSleep$snoreSound = null;

    @Unique
    private boolean ambientSleep$shouldDelay = true;

    @Unique
    private boolean ambientSleep$doesSnore = true;

    @Unique
    private boolean ambientSleep$firstSleepTick = true;

    @Unique
    private boolean ambientSleep$isFirstBreath = true;

    @Unique
    private int ambientSleep$breathDelay = Util.BREATH_DELAY;

    @Unique
    private int ambientSleep$snoreStartDelay = 0;

    @Unique
    private int ambientSleep$delayTimer = 0;

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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<?> type, Level level, CallbackInfo ci) {
        if (!level.isClientSide()) {
            return;
        }

        if (ambientSleep$me instanceof Player player) {
            ambientSleep$doesSnore = ambientSleep$doesEntitySnore(player, ModConfigs.INSTANCE.playerSnoreChance.get());
            ambientSleep$snoreSound = ModSounds.PLAYER_SNORE.get();
            if (player.isLocalPlayer()) {
                ambientSleep$shouldDelay = false;
                ambientSleep$breathDelay = 40;
            }
        }
        else if (ambientSleep$me instanceof Villager) {
            ambientSleep$doesSnore = ambientSleep$doesEntitySnore(ambientSleep$me, ModConfigs.INSTANCE.villagerSnoreChance.get());
            ambientSleep$snoreSound = ModSounds.VILLAGER_SNORE.get();
            ambientSleep$breathDelay = 80;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!level().isClientSide) {
            return;
        }

        if (isSleeping()) {
            if (ambientSleep$firstSleepTick) {
                ambientSleep$snoreStartDelay = (ambientSleep$shouldDelay ? Util.BREATH_DELAY + random.nextInt(40) : 0);
                ambientSleep$firstSleepTick = false;
                return;
            }

            if (ambientSleep$delayTimer < ambientSleep$snoreStartDelay) {
                ambientSleep$delayTimer++;
                return;
            }

            if (!ambientSleep$isFirstBreath && ambientSleep$breatheTimer < ambientSleep$breathDelay) {
                ambientSleep$breatheTimer++;
                return;
            }

            if (ambientSleep$snoreTimer >= Util.SNORE_DELAY) {
                if (ambientSleep$doesSnore && !isSilent() && ambientSleep$ZCount <= 0 && ambientSleep$snoreSound != null) {
                    Util.playClientSound(SoundSource.NEUTRAL, ambientSleep$me, ambientSleep$snoreSound, 1, getVoicePitch());
                }

                ambientSleep$snoreTimer = 0;
                ambientSleep$ZCount++;
                if (ModConfigs.INSTANCE.sleepingZs.get()) {
                    if ((ambientSleep$me instanceof Fox && ModConfigs.INSTANCE.foxesHaveSleepingZs.get())
                            || (!(ambientSleep$me instanceof Fox)
                            && (!ModConfigs.INSTANCE.displaySleepingZsOnlyWhenSnoring.get() || ambientSleep$doesSnore))) {
                        level().addParticle(ModParticles.SNORING.get(), getX(), getY() + 0.5, getZ(), 0, 0, 0);
                    }
                }

                if (ambientSleep$ZCount >= Util.MAX_Z_COUNT) {
                    ambientSleep$ZCount = 0;
                    ambientSleep$breatheTimer = 0;
                    ambientSleep$isFirstBreath = false;
                }
            }

            if (ambientSleep$snoreTimer < Util.SNORE_DELAY) {
                ambientSleep$snoreTimer++;
            }
        }
        else {
            ambientSleep$firstSleepTick = true;
            ambientSleep$isFirstBreath = true;
            ambientSleep$snoreStartDelay = 0;
            ambientSleep$delayTimer = 0;
            ambientSleep$breatheTimer = 0;
            ambientSleep$snoreTimer = 0;
            ambientSleep$ZCount = 0;
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

        ParticleSpawnUtil.spawnFallDustClouds(entity, 10, 10);
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
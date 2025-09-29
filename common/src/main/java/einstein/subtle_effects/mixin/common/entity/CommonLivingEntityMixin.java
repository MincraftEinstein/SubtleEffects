package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.networking.clientbound.ClientBoundDrankPotionPayload;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntityFellPacket;
import einstein.subtle_effects.networking.clientbound.ClientBoundEntitySpawnSprintingDustCloudsPacket;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class CommonLivingEntityMixin extends Entity {

    @Unique
    private boolean subtleEffects$validEntity;

    @Unique
    private float subtleEffects$minSpeed;

    @Unique
    private boolean subtleEffects$canStart = true;

    @Unique
    private Vec3 subtleEffects$lastCheckedPos;

    @Unique
    private final LivingEntity subtleEffects$me = (LivingEntity) (Object) this;

    public CommonLivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<?> type, Level level, CallbackInfo ci) {
        if (subtleEffects$me instanceof Ravager) {
            subtleEffects$validEntity = true;
            subtleEffects$minSpeed = 0.34F;
        }

        if (subtleEffects$me instanceof Goat) {
            subtleEffects$validEntity = true;
            subtleEffects$minSpeed = 0.4F;
        }

        if (subtleEffects$me instanceof Hoglin || subtleEffects$me instanceof Zoglin) {
            subtleEffects$validEntity = true;
            subtleEffects$minSpeed = 0.28F;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!level().isClientSide) {
            if (subtleEffects$me.isInvisible()) {
                return;
            }

            if (subtleEffects$canStart || position().distanceToSqr(subtleEffects$lastCheckedPos) > 0.5) {
                if (subtleEffects$validEntity) {
                    if (onGround() && subtleEffects$me.getSpeed() > subtleEffects$minSpeed) {
                        Services.NETWORK.sendToClientsTracking((ServerLevel) level(), subtleEffects$me.blockPosition(), new ClientBoundEntitySpawnSprintingDustCloudsPacket(getId()));
                        subtleEffects$lastCheckedPos = position();
                        subtleEffects$canStart = false;
                        return;
                    }
                    subtleEffects$lastCheckedPos = Vec3.ZERO;
                    subtleEffects$canStart = true;
                }
            }
        }
    }

    @ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
    private int calculateFallDamage(int damage, float distance, float damageMultiplier) {
        if (!subtleEffects$me.isInvisible()) {
            ParticleSpawnUtil.spawnFallDustClouds(subtleEffects$me, damageMultiplier, damage,
                    subtleEffects$me instanceof Player
                            ? ClientBoundEntityFellPacket.TypeConfig.PLAYER
                            : ClientBoundEntityFellPacket.TypeConfig.ENTITY
            );
        }
        return damage;
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 0))
    private boolean cancelFlyIntoWallServerCheck(boolean original) {
        return false;
    }

    @WrapWithCondition(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private boolean cancelFlyIntoWallClientSound(LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        return !level().isClientSide;
    }

    @WrapWithCondition(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean cancelFlyIntoWallClientHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof Player player && player.isCreative())) {
            if (!entity.isInvisible()) {
                ParticleSpawnUtil.spawnFallDustClouds(entity, 10, 10, ClientBoundEntityFellPacket.TypeConfig.ELYTRA);
            }
        }
        return !level().isClientSide;
    }

    @WrapOperation(method = "tickEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void replaceSlimeEffect(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if (level.isClientSide) {
            if (ModConfigs.ENTITIES.replaceOozingEffectParticles && options.getType().equals(ParticleTypes.ITEM_SLIME)) {
                level.addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()),
                        x, y, z, 0, 0, 0
                );
                return;
            }
            original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @Inject(method = "completeUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;triggerItemUseEffects(Lnet/minecraft/world/item/ItemStack;I)V"))
    private void spawnPotionParticles(CallbackInfo ci) {
        if (subtleEffects$me.isInvisible()) {
            return;
        }

        if (subtleEffects$me.level() instanceof ServerLevel level) {
            Services.NETWORK.sendToClientsTracking(subtleEffects$me instanceof ServerPlayer player ? player : null,
                    level, subtleEffects$me.blockPosition(), new ClientBoundDrankPotionPayload(subtleEffects$me.getId())
            );
            return;
        }
        ParticleSpawnUtil.spawnPotionRings(subtleEffects$me);
    }
}

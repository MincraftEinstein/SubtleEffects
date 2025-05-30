package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModDamageListeners;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import einstein.subtle_effects.util.EntityProvider;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin<T extends Entity> extends Entity {

    @Shadow
    public int hurtTime;

    @Shadow
    public int deathTime;

    @Unique
    private final LivingEntity subtleEffects$me = (LivingEntity) (Object) this;

    public ClientLivingEntityMixin(EntityType<T> type, Level level) {
        super(type, level);
    }

    @WrapOperation(method = "breakItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;spawnItemParticles(Lnet/minecraft/world/item/ItemStack;I)V"))
    private void increaseBreakItemParticles(LivingEntity entity, ItemStack stack, int particleCount, Operation<Void> original) {
        if (ModConfigs.ITEMS.increasedItemBreakParticles) {
            original.call(entity, stack, 16);
            return;
        }
        original.call(entity, stack, particleCount);
    }

    @Inject(method = "tickDeath", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;deathTime:I", ordinal = 0))
    private void deathTick(CallbackInfo ci) {
        if (!level().isClientSide) {
            return;
        }

        if (ModConfigs.ENTITIES.wardenDeathSoulParticles && getType().equals(EntityType.WARDEN)) {
            if (deathTime == 1 && !isRemoved()) {
                for (int i = 0; i < random.nextIntBetweenInclusive(3, 5); i++) {
                    level().addParticle(ParticleTypes.SCULK_SOUL,
                            position().x() + (getBbWidth() / 2) + MathUtil.nextNonAbsDouble(random),
                            position().y() + (getBbHeight() / 2) + MathUtil.nextNonAbsDouble(random),
                            position().z() + (getBbWidth() / 2) + MathUtil.nextNonAbsDouble(random),
                            0,
                            Mth.nextFloat(random, 0.01F, 0.1F),
                            0
                    );
                }
            }
        }
    }

    @Inject(method = "completeUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;triggerItemUseEffects(Lnet/minecraft/world/item/ItemStack;I)V"))
    private void spawnPotionParticles(CallbackInfo ci) {
        if (level().isClientSide) {
            Minecraft minecraft = Minecraft.getInstance();
            if (subtleEffects$me instanceof Player player) {
                if (player.equals(minecraft.player) && !ENTITIES.humanoids.potionRingsDisplayType.test(minecraft)) {
                    return;
                }
            }

            if (subtleEffects$me.isInvisible()) {
                return;
            }

            ItemStack useItem = subtleEffects$me.getUseItem();
            if (useItem.getItem() instanceof PotionItem) {
                Level level = subtleEffects$me.level();

                // noinspection all
                if (!PotionUtils.getMobEffects(useItem).isEmpty()) {
                    int color = PotionUtils.getColor(useItem);
                    level.addParticle(new ColorAndIntegerParticleOptions(ModParticles.POTION_EMITTER.get(), color, subtleEffects$me.getId()),
                            subtleEffects$me.getX(),
                            subtleEffects$me.getY(),
                            subtleEffects$me.getZ(),
                            0, 0, 0
                    );
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "hurt", at = @At("HEAD"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (level().isClientSide && !isInvulnerableTo(source) && amount > 0) {
            if (source.getEntity() instanceof LivingEntity && isAlive() && hurtTime == 0) {
                EntityType<T> type = (EntityType<T>) getType();
                if (ModDamageListeners.REGISTERED.containsKey(type)) {
                    ((EntityProvider<T>) ModDamageListeners.REGISTERED.get(type)).apply((T) this, level(), random);
                }
            }
        }
    }
}
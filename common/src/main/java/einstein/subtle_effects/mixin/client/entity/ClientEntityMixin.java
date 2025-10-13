package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModDamageListeners;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import einstein.subtle_effects.util.EntityAccessor;
import einstein.subtle_effects.util.EntityProvider;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.util.MathUtil.nextDouble;

@Mixin(Entity.class)
public abstract class ClientEntityMixin implements EntityAccessor {

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Unique
    private final Int2ObjectMap<EntityTicker<?>> subtleEffects$tickers = new Int2ObjectOpenHashMap<>();

    @Unique
    private double subtleEffects$nextCobwebSound = 0.5;

    @Unique
    private Vec3 subtleEffects$lastPos = Vec3.ZERO;

    @Unique
    private boolean subtleEffects$wasTouchingLava = false;

    @Shadow
    protected abstract boolean isInvulnerableToBase(DamageSource damageSource);

    @Inject(method = "playEntityOnFireExtinguishedSound", at = @At("TAIL"))
    private void addExtinguishParticles(CallbackInfo ci) {
        Level level = subtleEffects$me.level();

        if (level.isClientSide() && ModConfigs.ENTITIES.burning.extinguishSteam) {
            AABB boundingBox = subtleEffects$me.getBoundingBox();
            RandomSource random = subtleEffects$me.getRandom();

            for (int x = Mth.floor(boundingBox.minX); x < Mth.ceil(boundingBox.maxX); x++) {
                for (int y = Mth.floor(boundingBox.minY); y < Mth.ceil(boundingBox.maxY); y++) {
                    for (int z = Mth.floor(boundingBox.minZ); z < Mth.ceil(boundingBox.maxZ); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        FluidState fluidState = level.getFluidState(pos);
                        double fluidHeight = Math.max(Util.getCauldronFillHeight(state), fluidState.getHeight(level, pos));

                        if (fluidHeight > 0 && (fluidState.is(Fluids.WATER) || state.is(Blocks.WATER_CAULDRON))) {
                            BlockPos abovePos = pos.above();

                            if (!Util.isSolidOrNotEmpty(level, abovePos)) {

                                // More particles than 5 will be displayed, due to this method being called by multiple ticks
                                for (int i = 0; i < 5; i++) {
                                    level.addParticle(ModParticles.STEAM.get(),
                                            pos.getX() + random.nextDouble(),
                                            pos.getY() + fluidHeight + nextDouble(random, 0.5),
                                            pos.getZ() + random.nextDouble(),
                                            0, 0, 0
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "onInsideBlock", at = @At("HEAD"))
    private void inside(BlockState state, CallbackInfo ci) {
        if (subtleEffects$me.level().isClientSide()) {
            if (ModConfigs.BLOCKS.cobwebMovementSounds) {
                if (subtleEffects$me.flyDist > subtleEffects$nextCobwebSound && state.is(Blocks.COBWEB)) {
                    if (subtleEffects$lastPos.distanceToSqr(subtleEffects$me.position()) > 0.5) {
                        subtleEffects$nextCobwebSound += 0.5;
                        subtleEffects$lastPos = subtleEffects$me.position();

                        SoundType soundType = state.getSoundType();
                        Util.playClientSound(subtleEffects$me, soundType.getStepSound(), subtleEffects$me.getSoundSource(), soundType.getVolume() * 0.15F, soundType.getPitch());
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "hurtOrSimulate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtClient(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public <T extends Entity> void hurtClient(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (subtleEffects$me instanceof LivingEntity entity && !isInvulnerableToBase(source)) {
            if (source.getEntity() instanceof LivingEntity && entity.isAlive() && entity.hurtTime == 0) {
                EntityType<T> type = (EntityType<T>) entity.getType();
                if (ModDamageListeners.REGISTERED.containsKey(type)) {
                    ((EntityProvider<T>) ModDamageListeners.REGISTERED.get(type)).apply((T) (Object) this, entity.level(), entity.getRandom());
                }
            }
        }
    }

    @Inject(method = "doWaterSplashEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I"), cancellable = true)
    private void doWaterSplash(CallbackInfo ci) {
        Level level = subtleEffects$me.level();
        if (level.isClientSide()) {
            if (ParticleSpawnUtil.spawnSplashEffects(subtleEffects$me, level, ModParticles.WATER_SPLASH_EMITTER.get(), FluidTags.WATER, subtleEffects$me.getDeltaMovement())) {
                ci.cancel();
            }
        }
    }

    @Override
    public Int2ObjectMap<EntityTicker<?>> subtleEffects$getTickers() {
        return subtleEffects$tickers;
    }

    @Override
    public boolean subtleEffects$wasTouchingLava() {
        return subtleEffects$wasTouchingLava;
    }

    @Override
    public void subtleEffects$setTouchingLava(boolean touchingLava) {
        subtleEffects$wasTouchingLava = touchingLava;
    }
}

package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.particle.option.DirectionParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
                    Vec3 delta = subtleEffects$me.getDeltaMovement();
                    Vec3 position = subtleEffects$me.position();
                    BlockHitResult result = level.clip(new ClipContext(position,
                            position.add(delta),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            subtleEffects$me)
                    );

                    if (result.getType() != HitResult.Type.MISS) {
                        Direction direction = result.getDirection();
                        BlockPos pos = result.getBlockPos();
                        BlockState state = level.getBlockState(pos);
                        Vec3 location = result.getLocation();

                        if (!state.isAir()) {
                            Direction opposite = direction.getOpposite();
                            Direction.Axis axis = opposite.getAxis();
                            double offset = direction.getAxisDirection().getStep() * Mth.nextDouble(random, 0.001, 0.002);

                            level.addParticle(new DirectionParticleOptions(ModParticles.EGG_SPLAT.get(), opposite),
                                    axis == Direction.Axis.X ? Math.round(location.x()) + offset : location.x(),
                                    axis == Direction.Axis.Y ? Math.round(location.y()) + offset : location.y(),
                                    axis == Direction.Axis.Z ? Math.round(location.z()) + offset : location.z(),
                                    0, 0, 0
                            );
                        }
                    }
                }
            }
        }
    }
}

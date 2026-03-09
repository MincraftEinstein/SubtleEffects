package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.networking.clientbound.ClientBoundMobSpawnerSpawnPayload;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {

    @ModifyExpressionValue(method = "clientTick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;FLAME:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceFlameParticle(SimpleParticleType original) {
        if (ModConfigs.BLOCKS.purpleMonsterSpawnerParticles) {
            return ModParticles.PURPLE_FLAME.get();
        }
        return original;
    }

    @Inject(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private void clientTick(Level level, BlockPos pos, CallbackInfo ci, @Local RandomSource random) {
        if (ModConfigs.BLOCKS.sparks.monsterSpawnerSparks) {
            level.addParticle(SparkParticle.create(SparkType.SHORT_LIFE, random,
                            ModConfigs.BLOCKS.purpleMonsterSpawnerParticles
                                    ? SparkParticle.SPAWNER_COLORS
                                    : SparkParticle.DEFAULT_COLORS),
                    pos.getX() + random.nextDouble(),
                    pos.getY() + 0.5 + MathUtil.nextDouble(random, 0.5),
                    pos.getZ() + random.nextDouble(),
                    0, 0, 0
            );
        }

        float volume = ModConfigs.BLOCKS.monsterSpawnerAmbientSoundVolume.get();
        if (volume > 0 && random.nextFloat() <= 0.02F) {
            level.playLocalSound(pos, ModSounds.MONSTER_SPAWNER_AMBIENT.get(),
                    SoundSource.BLOCKS,
                    (random.nextFloat() * 0.25F + 0.75F) * volume,
                    random.nextFloat() + 0.5F,
                    false
            );
        }
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;spawnAnim()V"))
    private void onMobSpawn(ServerLevel level, BlockPos pos, CallbackInfo ci, @Local(ordinal = 1) BlockPos spawnPos) {
        Services.NETWORK.sendToClientsTracking(level, pos, new ClientBoundMobSpawnerSpawnPayload(spawnPos));
    }
}

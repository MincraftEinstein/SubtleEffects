package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.configs.ReplacedParticlesDisplayType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.SparkType;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;
import static net.minecraft.util.Mth.nextFloat;

@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {

    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "levelEvent", at = @At("TAIL"))
    private void levelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        if (level == null) {
            return;
        }

        RandomSource random = level.getRandom();
        BlockState state = level.getBlockState(pos);
        Player player = minecraft.player;

        switch (type) {
            case LevelEvent.SOUND_ANVIL_BROKEN: {
                if (BLOCKS.anvilBreakParticles) {
                    level.addDestroyBlockEffect(pos, Blocks.ANVIL.defaultBlockState());
                }
                break;
            }
            case LevelEvent.SOUND_ANVIL_USED: {
                if (BLOCKS.anvilUseParticles) {
                    for (int i = 0; i < 3; i++) {
                        TickerManager.schedule(8 * i, () ->
                                ParticleSpawnUtil.spawnHammeringWorkstationParticles(pos, random, level)
                        );
                    }
                }
                break;
            }
            case LevelEvent.SOUND_GRINDSTONE_USED: {
                if (BLOCKS.grindstoneUseParticles) {
                    if (state.hasProperty(GrindstoneBlock.FACING) && state.hasProperty(GrindstoneBlock.FACE)) {
                        Direction direction = state.getValue(GrindstoneBlock.FACING);
                        AttachFace face = state.getValue(GrindstoneBlock.FACE);
                        Direction side = face == AttachFace.CEILING ? Direction.DOWN : Direction.UP;

                        for (int i = 0; i < 20; i++) {
                            ParticleSpawnUtil.spawnParticlesOnSide(SparkParticle.create(SparkType.METAL, random), 0, side, level, pos, random,
                                    nextFloat(random, 0.1F, 0.2F) * (direction.getStepX() * 1.5),
                                    face == AttachFace.CEILING ? 0 : nextFloat(random, 0.1F, 0.2F),
                                    nextFloat(random, 0.1F, 0.2F) * (direction.getStepZ() * 1.5)
                            );
                        }
                    }
                }
                break;
            }
            case LevelEvent.SOUND_SMITHING_TABLE_USED: {
                if (BLOCKS.smithingTableUseParticles) {
                    ParticleSpawnUtil.spawnHammeringWorkstationParticles(pos, random, level);
                }
                break;
            }
            case 1503: {
                TickerManager.scheduleNext(() -> ParticleSpawnUtil.spawnEnderEyePlacementParticles(pos, random, level, Util.getEyeColorHolder(level, pos).toInt()));
                break;
            }
        }
    }

    @WrapOperation(method = "levelEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ParticleUtils;spawnParticlesOnBlockFaces(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/util/valueproviders/IntProvider;)V"))
    private void cancelOrReplaceCopperParticles(Level level, BlockPos pos, ParticleOptions particle, IntProvider count, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) int type) {
        BlockState state = level.getBlockState(pos);
        RandomSource random = level.getRandom();

        if (type == LevelEvent.PARTICLES_SCRAPE) {
            if (ModConfigs.ITEMS.axeScrapeParticlesDisplayType != ReplacedParticlesDisplayType.DEFAULT) {
                subtleEffects$spawnCopperParticles(level, pos, count, state, random);
            }
            return;
        }
        else if (type == LevelEvent.PARTICLES_WAX_OFF) {
            if (ModConfigs.ITEMS.axeWaxOffParticlesDisplayType != ReplacedParticlesDisplayType.DEFAULT) {
                subtleEffects$spawnCopperParticles(level, pos, count, state, random);
            }
            return;
        }

        original.call(level, pos, particle, count);
    }

    @Unique
    private static void subtleEffects$spawnCopperParticles(Level level, BlockPos pos, IntProvider count, BlockState state, RandomSource random) {
        ParticleSpawnUtil.spawnParticlesAroundShape(ParticleTypes.WAX_OFF,
                level, pos, state, count.sample(random),
                () -> new Vec3(
                        nextNonAbsDouble(random, 0.5),
                        nextNonAbsDouble(random, 0.5),
                        nextNonAbsDouble(random, 0.5)
                ), 0.125F
        );
    }

    // Fabric didn't like using a slice for some reason, should try again at some point
    @WrapWithCondition(method = "levelEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private boolean shouldSpawnEndPortalFrameSmoke(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @Local(argsOnly = true, ordinal = 0) int type) {
        if (type == LevelEvent.END_PORTAL_FRAME_FILL) {
            return BLOCKS.enderEyePlacedParticlesDisplayType != ModBlockConfigs.EnderEyePlacedParticlesDisplayType.DOTS;
        }
        return true;
    }

    @WrapOperation(method = "levelEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke(Operation<SimpleParticleType> original) {
        if (BLOCKS.steam.lavaFizzSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }

    @WrapOperation(method = "levelEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;CLOUD:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceCloud(Operation<SimpleParticleType> original) {
        if (BLOCKS.steam.spongeDryingOutSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }
}

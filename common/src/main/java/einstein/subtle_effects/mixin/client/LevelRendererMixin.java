package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.configs.ReplacedParticlesDisplayType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.*;
import static einstein.subtle_effects.util.MathUtil.nextDouble;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

@Mixin(value = LevelRenderer.class, priority = 999)
public abstract class LevelRendererMixin implements FrustumGetter {

    @Shadow
    @Nullable
    private ClientLevel level;

    @Shadow
    private Frustum cullingFrustum;

    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(method = "renderSnowAndRain", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LevelRenderer;RAIN_LOCATION:Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation replaceRainTexture(Operation<ResourceLocation> original) {
        if (ENVIRONMENT.biomeColorRain) {
            return Util.COLORLESS_RAIN_TEXTURE;
        }
        return original.call();
    }

    @WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer renderSnowAndRain(VertexConsumer instance, float red, float green, float blue, float alpha, Operation<VertexConsumer> original, @Local Biome biome, @Local Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN && ENVIRONMENT.biomeColorRain) {
            int waterColor = biome.getWaterColor();
            return instance.setColor((waterColor >> 16) / 255F, (waterColor >> 8) / 255F, waterColor / 255F, alpha);
        }
        return original.call(instance, red, green, blue, alpha);
    }

    @ModifyExpressionValue(method = "tickRain", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceRainEvaporationParticle(SimpleParticleType original) {
        if (BLOCKS.steam.replaceRainEvaporationSteam) {
            return ModParticles.STEAM.get();
        }
        return original;
    }

    // 'original' does not capture the '!', so the returned expression must be written inverted
    @ModifyExpressionValue(method = "tickRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean modifyRainEvaporationBlocks(boolean original, @Local BlockState state) {
        return original || (BLOCKS.steam.lavaCauldronsEvaporateRain && state.is(Blocks.LAVA_CAULDRON));
    }

    @ModifyExpressionValue(method = "tickRain", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;RAIN:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceRainParticle(SimpleParticleType original, @Local FluidState fluidState, @Local BlockState state) {
        if (BLOCKS.rainWaterRipples && (fluidState.is(FluidTags.WATER) || state.is(Blocks.WATER_CAULDRON))) {
            return ModParticles.WATER_RIPPLE.get();
        }
        return original;
    }

    @WrapOperation(method = "tickRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void modifyCauldronRippleParticlePos(ClientLevel level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original, @Local BlockState state, @Local RandomSource random, @Local(ordinal = 1) BlockPos pos) {
        if (BLOCKS.rainWaterRipples) {
            if (random.nextDouble() > BLOCKS.rainWaterRipplesDensity.get()) {
                return;
            }

            if (options.getType().equals(ModParticles.WATER_RIPPLE.get()) && state.is(Blocks.WATER_CAULDRON)) {
                x = pos.getX() + 0.1875 + nextDouble(random, 0.625);
                y = pos.getY() + Util.getCauldronFillHeight(state) + 0.01;
                z = pos.getZ() + 0.1875 + nextDouble(random, 0.625);
            }
        }
        original.call(level, options, x, y, z, xSpeed, ySpeed, zSpeed);
    }

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
                    level.addDestroyBlockEffect(pos, state);
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
                ParticleSpawnUtil.spawnGrindstoneUsedParticles(level, pos, state, random);
                break;
            }
            case LevelEvent.SOUND_SMITHING_TABLE_USED: {
                if (BLOCKS.smithingTableUseParticles) {
                    ParticleSpawnUtil.spawnHammeringWorkstationParticles(pos, random, level);
                }
                break;
            }
            case LevelEvent.END_PORTAL_FRAME_FILL: {
                TickerManager.scheduleNext(() -> ParticleSpawnUtil.spawnEnderEyePlacementParticles(pos, random, level, Util.getEyeColorHolder(level, pos).toInt()));
                break;
            }
        }
    }

    @WrapOperation(method = "levelEvent",
            slice = @Slice(
                    to = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;SPLASH_POTION_BREAK:Lnet/minecraft/sounds/SoundEvent;"),
                    from = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;EFFECT:Lnet/minecraft/core/particles/SimpleParticleType;")
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)Lnet/minecraft/client/particle/Particle;"
            )
    )
    private Particle replaceSplashPotionParticles(LevelRenderer levelRenderer, ParticleOptions options, boolean force, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Particle> original, @Local(ordinal = 0) float red, @Local(ordinal = 1) float green, @Local(ordinal = 2) float blue) {
        if (ITEMS.splashPotionClouds && level != null) {
            RandomSource random = level.getRandom();

            if (random.nextInt(5) == 0) {
                double powerModifier = random.nextDouble() * 4;
                // equivalent of particle.setPower, but since this is done before initial particle velocity is set the outcome is different
                xSpeed *= powerModifier;
                ySpeed = (ySpeed - 0.1) * powerModifier + 0.1;
                zSpeed *= powerModifier;

                if (random.nextInt(3) == 0) {
                    original.call(levelRenderer,
                            ColorParticleOption.create(ModParticles.POTION_POOF_CLOUD.get(), red, green, blue),
                            false, x, y, z, xSpeed, ySpeed, zSpeed
                    );
                }

                Particle particle = original.call(levelRenderer, options, force, x, y, z, xSpeed, ySpeed, zSpeed);
                if (particle != null) {
                    particle.setColor(red, green, blue);
                }
            }

            // always returning null to prevent calling particle.setPower
            return null;
        }
        return original.call(levelRenderer, options, force, x, y, z, xSpeed, ySpeed, zSpeed);
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
    @WrapWithCondition(method = "levelEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private boolean shouldSpawnEndPortalFrameSmoke(ClientLevel level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @Local(argsOnly = true, ordinal = 0) int type) {
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

    @ModifyReturnValue(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "RETURN", ordinal = 0))
    private Particle spawnForcedParticle(Particle particle) {
        if (particle != null) {
            ((ParticleAccessor) particle).subtleEffects$force();
        }
        return particle;
    }

    @Inject(method = "shootParticles", at = @At("TAIL"))
    private void addBubbles(int data, BlockPos pos, RandomSource random, SimpleParticleType type, CallbackInfo ci, @Local Direction direction, @Local(ordinal = 0) double xOffset, @Local(ordinal = 1) double yOffset, @Local(ordinal = 2) double zOffset) {
        if (!BLOCKS.dispenseItemBubbles) {
            return;
        }

        // noinspection all
        FluidState fluidState = level.getFluidState(pos.relative(direction));
        if (fluidState.is(FluidTags.WATER)) {
            int stepX = direction.getStepX();
            int stepY = direction.getStepY();
            int stepZ = direction.getStepZ();

            for (int i = 0; i < 10; ++i) {
                double speedModifier = random.nextDouble() * 0.2 + 0.01;
                double x = xOffset + stepX * 0.01 + (random.nextDouble() - 0.5) * stepZ * 0.5;
                double y = yOffset + stepY * 0.01 + (random.nextDouble() - 0.5) * stepY * 0.5;
                double z = zOffset + stepZ * 0.01 + (random.nextDouble() - 0.5) * stepX * 0.5;
                double xSpeed = stepX * speedModifier + random.nextGaussian() * 0.01;
                double ySpeed = stepY * speedModifier + random.nextGaussian() * 0.01;
                double zSpeed = stepZ * speedModifier + random.nextGaussian() * 0.01;

                level.addParticle(ParticleTypes.BUBBLE, x, y, z, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    @Override
    public Frustum subtleEffects$getCullingFrustum() {
        return cullingFrustum;
    }
}

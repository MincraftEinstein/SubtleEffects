package einstein.subtle_effects.mixin.client;

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
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;
import static net.minecraft.util.Mth.nextFloat;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements FrustumGetter {

    private static final Random STAR_RANDOM = new Random(10842L);
    
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

    @Inject(method = "levelEvent", at = @At("TAIL"))
    private void levelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        if (level == null) {
            return;
        }

        RandomSource random = level.getRandom();
        BlockState state = level.getBlockState(pos);
        Player player = minecraft.player;

        switch (type) {
            case 1029: {
                if (BLOCKS.anvilBreakParticles) {
                    level.addDestroyBlockEffect(pos, state);
                }
                break;
            }
            case 1030: {
                if (BLOCKS.anvilUseParticles) {
                    for (int i = 0; i < 3; i++) {
                        TickerManager.schedule(8 * i, () ->
                                ParticleSpawnUtil.spawnHammeringWorkstationParticles(pos, random, level)
                        );
                    }
                }
                break;
            }
            case 1042: {
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

    @Override
    public Frustum subtleEffects$getCullingFrustum() {
        return cullingFrustum;
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/LevelRenderer;renderStars(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",shift = At.Shift.AFTER))
    private void injectTwinklingStars(PoseStack poseStack, Matrix4f projectionMatrix, float partialTicks, CallbackInfo ci) {
        if (ENVIRONMENT.twinklingStars) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;

        if (level == null || !level.dimensionType().hasSkyLight()) return;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.begin(VertexFormat.Mode.POINTS, DefaultVertexFormat.POSITION_COLOR);

        double time = (level.getGameTime() + partialTicks) * 0.04;

        for (int i = 0; i < 1200; ++i) {
            double x = (STAR_RANDOM.nextDouble() - 0.5) * 800.0;
            double y = (STAR_RANDOM.nextDouble() - 0.5) * 800.0;
            double z = (STAR_RANDOM.nextDouble() - 0.5) * 800.0;

            float alpha = 0.6f + 0.4f * Mth.sin((float) (time + i * 0.1));
            buffer.vertex(x, y, z).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
        }

        tesselator.end();
        RenderSystem.depthMask(true);
        }
    }
}

package einstein.subtle_effects.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.EndRemasteredCompat;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.data.MobSkullShaderData;
import einstein.subtle_effects.data.MobSkullShaderReloadListener;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.client.GameRendererAccessor;
import einstein.subtle_effects.mixin.common.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.particle.EnderEyePlacedRingParticle;
import einstein.subtle_effects.particle.option.DropletParticleOptions;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int PARTICLE_LIGHT_COLOR = 240;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(Vec3.fromRGB24(0xFFBC5E).toVector3f(), 1);
    public static final ResourceLocation COLORLESS_RAIN_TEXTURE = SubtleEffects.loc("textures/environment/colorless_rain.png");
    public static final Gson GSON = new GsonBuilder().create();
    public static final ResourceLocation VANILLA_EYE = ResourceLocation.withDefaultNamespace("ender_eye");
    private static final String UUID = "d71e4b41-9315-499f-a934-ca925421fb38";

    public static void playClientSound(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level != null && level.isClientSide) {
            level.playSound(minecraft.player, entity, sound, source, volume, pitch);
        }
    }

    public static void playClientSound(BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level != null && level.isClientSide) {
            level.playSound(minecraft.player, pos.getX(), pos.getY(), pos.getZ(), sound, source, volume, pitch);
        }
    }

    public static boolean isSolidOrNotEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender(level, pos) || !level.getFluidState(pos).isEmpty();
    }

    public static void applyHelmetShader(ItemStack stack, CameraType cameraType) {
        if (!cameraType.isFirstPerson()) {
            return;
        }

        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        for (MobSkullShaderData shaderData : MobSkullShaderReloadListener.MOB_SKULL_SHADERS.values()) {
            if (shaderData.stackHolder().matches(stack)) {
                loadShaderEffect(shaderData.shaderId(), gameRenderer);
                return;
            }
        }
        gameRenderer.shutdownEffect();
    }

    private static void loadShaderEffect(ResourceLocation shaderId, GameRenderer gameRenderer) {
        PostChain effect = gameRenderer.currentEffect();
        if (effect == null || !effect.getName().equals(shaderId.toString())) {
            ((GameRendererAccessor) gameRenderer).loadShaderEffect(shaderId);
        }
    }

    public static void setColorFromHex(Particle particle, int hexColor) {
        particle.setColor((hexColor >> 16) / 255F, (hexColor >> 8) / 255F, hexColor / 255F);
    }

    public static boolean isChunkLoaded(Level level, double blockX, double blockZ) {
        return level.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(blockX), SectionPos.blockToSectionCoord(blockZ));
    }

    public static double getCauldronFillHeight(BlockState state) {
        if (state.getBlock() instanceof AbstractCauldronBlock block) {
            return ((AbstractCauldronBlockAccessor) block).getFillHeight(state);
        }
        return 0;
    }

    public static Fluid getCauldronFluid(BlockState state) {
        if (state.getBlock() instanceof AbstractCauldronBlock block) {
            FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) block).subtleEffects$getFluidDefinition();
            if (fluidDefinition != null) {
                return fluidDefinition.source();
            }
        }
        return Fluids.EMPTY;
    }

    @Nullable
    public static ParticleOptions getParticleForFluid(Fluid fluid) {
        FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluid).subtleEffects$getFluidDefinition();
        if (fluidDefinition != null) {
            return new DropletParticleOptions(fluidDefinition.id(), false, 1, false);
        }
        return null;
    }

    @Nullable
    public static ParticleOptions getCauldronParticle(BlockState state) {
        Fluid fluid = getCauldronFluid(state);
        if (!fluid.isSame(Fluids.EMPTY)) {
            return getParticleForFluid(fluid);
        }
        else if (state.is(Blocks.POWDER_SNOW_CAULDRON)) {
            return ModParticles.SNOW.get();
        }
        return null;
    }

    public static ValidatedColor.ColorHolder getEyeColorHolder(Level level, BlockPos pos) {
        if (CompatHelper.IS_END_REMASTERED_LOADED.get()) {
            ValidatedColor.ColorHolder color = EndRemasteredCompat.getEyeColor(level, pos);

            if (color != null) {
                return color;
            }
        }

        ValidatedColor.ColorHolder vanillaColor = BLOCKS.eyeColors.get().get(VANILLA_EYE);
        if (vanillaColor != null) {
            return vanillaColor;
        }

        return toColorHolder(EnderEyePlacedRingParticle.DEFAULT_COLOR);
    }

    public static ValidatedColor.ColorHolder toColorHolder(int color) {
        return new ValidatedColor.ColorHolder((color >> 16) & 255, (color >> 8) & 255, color & 255, 255, false);
    }

    public static float randomizeColor(RandomSource random, float color, float multiplier) {
        return (random.nextFloat() * 0.2F + 0.8F) * color * multiplier;
    }

    public static void setRandomizedColor(Particle particle, RandomSource random, float r, float g, float b) {
        float multiplier = random.nextFloat() * 0.4F + 0.6F;
        particle.setColor(
                randomizeColor(random, r, multiplier),
                randomizeColor(random, g, multiplier),
                randomizeColor(random, b, multiplier)
        );
    }

    public static float getPartialTicks() {
        return getPartialTicks(false);
    }

    public static float getPartialTicks(boolean runsNormally) {
        return Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(runsNormally);
    }

    public static boolean isMincraftEinstein(AbstractClientPlayer player) {
        return player.getStringUUID().equals(UUID);
    }

    public static String formatOrdinal(long number) {
        String stringValue = String.valueOf(number);
        if (stringValue.endsWith("1") && !stringValue.endsWith("11")) {
            return stringValue + "st";
        }
        else if (stringValue.endsWith("2") && !stringValue.endsWith("12")) {
            return stringValue + "nd";
        }
        else if (stringValue.endsWith("3") && !stringValue.endsWith("13")) {
            return stringValue + "rd";
        }
        return stringValue + "th";
    }

    public static boolean isRainingAt(Level level, BlockPos pos) {
        if (level.isRaining() && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) <= pos.getY()) {
            return level.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN;
        }
        return false;
    }

    public static boolean isSouthEast(CharSequence searchQuery) {
        return "south east".contains(searchQuery) || "south_east".contains(searchQuery) || "se".contains(searchQuery);
    }

    public static boolean isHarmful(PotionContents contents) {
        final int[] harmful = {0};
        final int[] notHarmful = {0};

        contents.forEachEffect(effect -> {
            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                harmful[0]++;
                return;
            }
            notHarmful[0]++;
        });

        return harmful[0] > notHarmful[0];
    }

    public static boolean isUnderwater(Level level, BlockPos pos) {
        return level.isWaterAt(pos) && level.getBlockState(pos.above()).is(Blocks.WATER);
    }

    public static boolean isNotAboveUpwardsBubbleColumn(Level level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return !belowState.is(Blocks.SOUL_SAND) && (!belowState.is(Blocks.BUBBLE_COLUMN) || belowState.getValue(BubbleColumnBlock.DRAG_DOWN));
    }

    public static boolean isAboveDownwardsBubbleColumn(Level level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return belowState.is(Blocks.MAGMA_BLOCK) || (belowState.is(Blocks.BUBBLE_COLUMN) && belowState.getValue(BubbleColumnBlock.DRAG_DOWN));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static <T extends Entity> void getAdjustedNameTagPosition(PoseStack poseStack, double x, double y, double z, Operation<Void> original, T entity) {
        if (!ModConfigs.ENTITIES.sleeping.adjustNameTagWhenSleeping) {
            original.call(poseStack, x, y, z);
            return;
        }

        if (entity instanceof LivingEntity livingEntity && livingEntity.isSleeping()) {
            Level level = entity.level();
            Optional<BlockPos> pos = livingEntity.getSleepingPos();

            if (pos.isPresent()) {
                BlockState state = level.getBlockState(pos.get());

                if (state.hasProperty(BedBlock.OCCUPIED) && state.getValue(BedBlock.OCCUPIED)) {
                    Direction facing = state.getValue(BedBlock.FACING);

                    switch (facing) {
                        case NORTH -> poseStack.translate(z, x, -y);
                        case SOUTH -> poseStack.translate(z, x, y);
                        case EAST -> poseStack.translate(y, z, x);
                        case WEST -> poseStack.translate(-y, z, x);
                        default -> original.call(poseStack, x, y, z);
                    }
                    return;
                }
            }
        }
        original.call(poseStack, x, y, z);
    }
}

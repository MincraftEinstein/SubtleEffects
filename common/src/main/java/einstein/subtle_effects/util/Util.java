package einstein.subtle_effects.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.EndRemasteredCompat;
import einstein.subtle_effects.data.MobSkullShaderData;
import einstein.subtle_effects.data.MobSkullShaderReloadListener;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.client.GameRendererAccessor;
import einstein.subtle_effects.mixin.client.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.particle.EnderEyePlacedRingParticle;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int PARTICLE_LIGHT_COLOR = 240;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(0xFFBC5E, 1);
    public static final ResourceLocation COLORLESS_RAIN_TEXTURE = SubtleEffects.loc("textures/environment/colorless_rain.png");
    public static final Gson GSON = new GsonBuilder().create();
    public static final ResourceLocation VANILLA_EYE = ResourceLocation.withDefaultNamespace("ender_eye");

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
        return level.getBlockState(pos).isSolidRender() || !level.getFluidState(pos).isEmpty();
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
        gameRenderer.clearPostEffect();
    }

    private static void loadShaderEffect(ResourceLocation shaderId, GameRenderer gameRenderer) {
        ResourceLocation effect = gameRenderer.currentPostEffect();
        if (effect == null || !effect.equals(shaderId)) {
            ((GameRendererAccessor) gameRenderer).setShaderEffect(shaderId);
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
        if (state.is(Blocks.LAVA_CAULDRON)) {
            return Fluids.LAVA;
        }
        else if (state.is(Blocks.WATER_CAULDRON)) {
            return Fluids.WATER;
        }
        return Fluids.EMPTY;
    }

    @Nullable
    public static ParticleOptions getParticleForFluid(Fluid fluid) {
        if (fluid.isSame(Fluids.WATER)) {
            return ParticleTypes.SPLASH;
        }
        else if (fluid.isSame(Fluids.LAVA)) {
            return ModParticles.LAVA_SPLASH.get();
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

        ValidatedColor.ColorHolder vanillaColor = BLOCKS.eyeColors.get(VANILLA_EYE);
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
}

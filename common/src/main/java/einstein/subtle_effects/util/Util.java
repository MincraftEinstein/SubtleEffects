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
import einstein.subtle_effects.particle.option.SplashDropletParticleOptions;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
            return new SplashDropletParticleOptions(ModParticles.WATER_SPLASH_DROPLET.get(), 1);
        }
        else if (fluid.isSame(Fluids.LAVA)) {
            return new SplashDropletParticleOptions(ModParticles.LAVA_SPLASH_DROPLET.get(), 1);
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

    public static float getPartialTicks() {
        return getPartialTicks(false);
    }

    public static float getPartialTicks(boolean runsNormally) {
        return Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(runsNormally);
    }

    public static boolean isMincraftEinstein(AbstractClientPlayer player) {
        return player.getStringUUID().equals(UUID);
    }

    public static String getOrdinal(long number) {
        return number + (number == 1 ? "st" : number == 2 ? "nd" : number == 3 ? "rd" : "th");
    }

    public static boolean isRainingAt(Level level, BlockPos pos) {
        if (level.isRaining() && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) <= pos.getY()) {
            return level.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN;
        }
        return false;
    }
}

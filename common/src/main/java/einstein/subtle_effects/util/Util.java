package einstein.subtle_effects.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.EndRemasteredCompat;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.data.MobSkullShaderData;
import einstein.subtle_effects.data.MobSkullShaderReloadListener;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.client.GameRendererAccessor;
import einstein.subtle_effects.mixin.client.block.AbstractCauldronBlockAccessor;
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
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
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
    public static final ResourceLocation VANILLA_EYE = new ResourceLocation("ender_eye");
    private static final String UUID = "d71e4b41-9315-499f-a934-ca925421fb38";
    public static final Codec<Integer> RGB_COLOR_CODEC = Codec.either(Codec.either(Codec.INT, ExtraCodecs.VECTOR3F).xmap(either ->
            either.map(
                    i -> i,
                    color -> FastColor.ARGB32.color(255, Mth.floor(color.x() * 255), Mth.floor(color.y() * 255), Mth.floor(color.z() * 255))
            ), Either::left
    ), Codec.STRING).comapFlatMap(either -> either.map(DataResult::success, string -> {
        try {
            return DataResult.success(Integer.decode(string));
        }
        catch (NumberFormatException e) {
            return DataResult.error(() -> "String '" + string + "' is not a valid integer color");
        }
    }), Either::left);
    public static final Codec<SimpleParticleType> SIMPLE_PARTICLE_TYPE_CODEC = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap(options -> {
        if (options instanceof SimpleParticleType particle) {
            return DataResult.success(particle);
        }
        return DataResult.error(() -> "Particle type is not a simple particle type: " + options);
    }, particle -> particle);

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
        return Minecraft.getInstance().getFrameTime();
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

    public static Codec<Either<Float, Boolean>> configurableFloatCodec(String floatName) {
        return Codec.either(
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.floatRange(0, 1).fieldOf(floatName).forGetter(Float::floatValue)
                ).apply(instance, Float::floatValue)),
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.BOOL.fieldOf("use_config").forGetter(Boolean::booleanValue)
                ).apply(instance, Boolean::booleanValue))
        );
    }

    public static boolean isSouthEast(CharSequence searchQuery) {
        return "south east".contains(searchQuery) || "south_east".contains(searchQuery);
    }
}

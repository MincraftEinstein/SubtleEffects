package einstein.subtle_effects.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.common.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.particle.option.DropletParticleOptions;
import einstein.subtle_effects.util.accesssors.FluidDefinitionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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

    public static boolean isSolidOrNotEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender(level, pos) || !level.getFluidState(pos).isEmpty();
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

    public static boolean isMincraftEinstein(Player player) {
        return player.getStringUUID().equals(UUID);
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
}

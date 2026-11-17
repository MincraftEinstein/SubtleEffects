package einstein.subtle_effects.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.mixin.client.entity.AbstractHorseAccessor;
import einstein.subtle_effects.mixin.common.block.AbstractCauldronBlockAccessor;
import einstein.subtle_effects.particle.option.DropletParticleOptions;
import einstein.subtle_effects.util.accesssors.FluidDefinitionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int PARTICLE_LIGHT_COLOR = 240;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(0xFFBC5E, 1);
    public static final Identifier COLORLESS_RAIN_TEXTURE = SubtleEffects.loc("textures/environment/colorless_rain.png");
    public static final Gson GSON = new GsonBuilder().create();
    public static final Identifier VANILLA_EYE = Identifier.withDefaultNamespace("ender_eye");
    private static final String UUID = "d71e4b41-9315-499f-a934-ca925421fb38";

    public static boolean isSolidOrNotEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender() || !level.getFluidState(pos).isEmpty();
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

    public static SoundEvent getEntityEatSound(LivingEntity entity, ItemStack stack) {
        if (entity instanceof AbstractHorse horse) {
            SoundEvent horseEatSound = ((AbstractHorseAccessor) horse).getEatSound();
            if (horseEatSound != null && !horseEatSound.equals(SoundEvents.GENERIC_EAT.value())) {
                return horseEatSound;
            }
        }

        SoundEvent eatSound = null;
        if (entity instanceof Consumable.OverrideConsumeSound soundOverride) {
            eatSound = soundOverride.getConsumeSound(stack);
        }

        SoundEvent stackEatSound = null;
        if (stack.has(DataComponents.CONSUMABLE)) {
            // noinspection all
            stackEatSound = stack.get(DataComponents.CONSUMABLE).sound().value();
        }

        if ((eatSound != null && !SoundEvents.GENERIC_EAT.value().equals(eatSound)) && !eatSound.equals(stackEatSound)) {
            return eatSound;
        }
        else if (entity instanceof Strider) {
            return SoundEvents.STRIDER_EAT;
        }
        else if (entity instanceof Parrot) {
            return SoundEvents.PARROT_EAT;
        }
        return stackEatSound != null ? stackEatSound : SoundEvents.GENERIC_EAT.value();
    }

    public static boolean isMincraftEinstein(String uuid) {
        return UUID.equals(uuid);
    }

    public static boolean isRainingAt(Level level, BlockPos pos) {
        if (level.isRaining() && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) <= pos.getY()) {
            return level.getBiome(pos).value().getPrecipitationAt(pos, level.getSeaLevel()) == Biome.Precipitation.RAIN;
        }
        return false;
    }

    public static ItemStackTemplate template(ItemStack stack) {
        return ItemStackTemplate.fromNonEmptyStack(stack);
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
        }, 0);

        return harmful[0] > notHarmful[0];
    }

    public static boolean isEntityInsideContent(BlockState state, BlockPos pos, Entity entity) {
        Block block = state.getBlock();
        if (block instanceof AbstractCauldronBlockAccessor cauldronBlock) {
            return entity.getY() < pos.getY() + cauldronBlock.getFillHeight(state) && entity.getBoundingBox().maxY > pos.getY() + 0.25;
        }
        return false;
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

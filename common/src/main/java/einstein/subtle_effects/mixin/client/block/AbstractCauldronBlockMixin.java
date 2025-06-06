package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    @ModifyReturnValue(method = "useItemOn", at = @At("RETURN"))
    private ItemInteractionResult spawnInteractionParticles(ItemInteractionResult result, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos, @Local CauldronInteraction interaction) {
        if (level.isClientSide && result == ItemInteractionResult.SUCCESS) {
            RandomSource random = level.getRandom();

            TickerManager.schedule(2, () -> {
                BlockState newState = level.getBlockState(pos);
                boolean isEmpty = newState.is(Blocks.CAULDRON);
                double fluidHeight = Util.getCauldronFillHeight(isEmpty ? state : newState);
                ParticleOptions particle = subtleEffects$getCauldronParticle(isEmpty ? state : newState);

                if (fluidHeight > 0 && particle != null) {
                    for (int i = 0; i < 16; i++) {
                        level.addParticle(
                                particle,
                                pos.getX() + random.nextDouble(),
                                pos.getY() + fluidHeight,
                                pos.getZ() + random.nextDouble(),
                                0, 0, 0
                        );
                    }
                }

                if (interaction.equals(CauldronInteraction.DYED_ITEM) || interaction.equals(CauldronInteraction.BANNER) || interaction.equals(CauldronInteraction.SHULKER_BOX)) {
                    Util.playClientSound(pos, ModSounds.CAULDRON_CLEAN_ITEM.get(), SoundSource.BLOCKS, 1, (random.nextFloat() - random.nextFloat()) * 0.2F + 1);
                }
            });
        }
        return result;
    }

    @Unique
    @Nullable
    private static ParticleOptions subtleEffects$getCauldronParticle(BlockState state) {
        Fluid fluid = Util.getCauldronFluid(state);
        if (!fluid.isSame(Fluids.EMPTY)) {
            return Util.getParticleForFluid(fluid);
        }
        else if (state.is(Blocks.POWDER_SNOW_CAULDRON)) {
            return ModParticles.SNOW.get();
        }
        return null;
    }
}

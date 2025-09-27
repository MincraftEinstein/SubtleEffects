package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextDouble;
import static einstein.subtle_effects.util.MathUtil.nextSign;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    @ModifyReturnValue(method = "useItemOn", at = @At("RETURN"))
    private ItemInteractionResult spawnInteractionParticles(ItemInteractionResult result, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos, @Local CauldronInteraction interaction) {
        if (level.isClientSide && result == ItemInteractionResult.SUCCESS) {
            RandomSource random = level.getRandom();

            if (BLOCKS.cauldronUseParticles || BLOCKS.cauldronCleanItemSounds) {
                TickerManager.schedule(2, () -> {
                    if (BLOCKS.cauldronUseParticles) {
                        if (level.getFluidState(pos.above()).getAmount() >= 5) {
                            return;
                        }

                        BlockState newState = level.getBlockState(pos);
                        boolean isEmpty = newState.is(Blocks.CAULDRON);
                        double fluidHeight = Util.getCauldronFillHeight(isEmpty ? state : newState);
                        ParticleOptions particle = Util.getCauldronParticle(isEmpty ? state : newState);

                        if (fluidHeight > 0 && particle != null) {
                            for (int i = 0; i < 16; i++) {
                                int xSign = nextSign(random);
                                int zSign = nextSign(random);

                                level.addParticle(
                                        particle,
                                        pos.getX() + 0.5 + nextDouble(random, 0.5) * xSign,
                                        pos.getY() + fluidHeight,
                                        pos.getZ() + 0.5 + nextDouble(random, 0.5) * xSign,
                                        nextDouble(random, 0.15F) * xSign,
                                        nextDouble(random, 0.35F),
                                        nextDouble(random, 0.15F) * zSign
                                );
                            }
                        }
                    }

                    if (BLOCKS.cauldronCleanItemSounds) {
                        if (interaction.equals(CauldronInteraction.DYED_ITEM) || interaction.equals(CauldronInteraction.BANNER) || interaction.equals(CauldronInteraction.SHULKER_BOX)) {
                            Util.playClientSound(pos, ModSounds.CAULDRON_CLEAN_ITEM.get(), SoundSource.BLOCKS, 1, (random.nextFloat() - random.nextFloat()) * 0.2F + 1);
                        }
                    }
                });
            }
        }
        return result;
    }
}

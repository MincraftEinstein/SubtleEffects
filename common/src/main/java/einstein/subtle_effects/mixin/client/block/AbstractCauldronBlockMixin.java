package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    @ModifyReturnValue(method = "useItemOn", at = @At("RETURN"))
    private InteractionResult spawnInteractionParticles(InteractionResult result, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) ItemStack stack) {
        if (level.isClientSide && result == InteractionResult.SUCCESS) {
            RandomSource random = level.getRandom();

            if (BLOCKS.cauldronUseParticles || BLOCKS.cauldronCleanItemSounds) {
                TickerManager.schedule(2, () -> {
                    if (BLOCKS.cauldronUseParticles) {
                        BlockState newState = level.getBlockState(pos);
                        boolean isEmpty = newState.is(Blocks.CAULDRON);
                        double fluidHeight = Util.getCauldronFillHeight(isEmpty ? state : newState);
                        ParticleOptions particle = Util.getCauldronParticle(isEmpty ? state : newState);

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
                    }

                    if (BLOCKS.cauldronCleanItemSounds) {
                        if ((stack.is(ItemTags.DYEABLE) && stack.has(DataComponents.DYED_COLOR))
                                || (stack.has(DataComponents.BANNER_PATTERNS) && !stack.get(DataComponents.BANNER_PATTERNS).layers().isEmpty())
                                || (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock)) {
                            Util.playClientSound(pos, ModSounds.CAULDRON_CLEAN_ITEM.get(), SoundSource.BLOCKS, 1, (random.nextFloat() - random.nextFloat()) * 0.2F + 1);
                        }
                    }
                });
            }
        }
        return result;
    }
}

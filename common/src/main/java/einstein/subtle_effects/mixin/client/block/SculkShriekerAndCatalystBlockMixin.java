package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SculkCatalystBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({SculkShriekerBlock.class, SculkCatalystBlock.class})
public abstract class SculkShriekerAndCatalystBlockMixin extends BaseEntityBlock {

    @Unique
    private final Object subtleEffects$me = this;

    protected SculkShriekerAndCatalystBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            if ((ModConfigs.BLOCKS.sculkShriekerDestroySouls && subtleEffects$me instanceof SculkShriekerBlock)
                    || (ModConfigs.BLOCKS.sculkCatalystDestroySouls && subtleEffects$me instanceof SculkCatalystBlock)
            ) {
                level.addParticle(ParticleTypes.SCULK_SOUL,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        0,
                        Mth.randomBetween(level.getRandom(), 0.01F, 0.1F),
                        0
                );
            }
        }
    }
}

package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "addGrowthParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private static void spawnParticles(LevelAccessor accessor, BlockPos pos, int data, CallbackInfo ci, @Local BlockState state) {
        if (ITEMS.boneMealUsingParticles) {
            BlockPos belowPos = pos.below();
            boolean solidRender = state.isSolidRender(accessor, belowPos);

            if (solidRender || state.is(Blocks.WATER)) {
                subtleEffects$spawnParticles(accessor, solidRender ? pos : belowPos, data, 3, 1, false, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.BONE_MEAL)));
                return;
            }

            subtleEffects$spawnParticleInBlock(accessor, pos, data / 3, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.BONE_MEAL)));
        }
    }

    @Unique
    private static void subtleEffects$spawnParticleInBlock(LevelAccessor level, BlockPos pos, int count, ParticleOptions particle) {
        BlockState state = level.getBlockState(pos);
        double y = state.isAir() ? 1 : state.getShape(level, pos).max(Direction.Axis.Y);
        subtleEffects$spawnParticles(level, pos, count, 0.5, y, true, particle);
    }

    @Unique
    private static void subtleEffects$spawnParticles(LevelAccessor level, BlockPos pos, int count, double xzSpread, double ySpread, boolean allowInAir, ParticleOptions particle) {
        RandomSource random = level.getRandom();

        for (int i = 0; i < count; ++i) {
            double d0 = random.nextGaussian() * 0.02;
            double d1 = random.nextGaussian() * 0.02;
            double d2 = random.nextGaussian() * 0.02;
            double d3 = 0.5 - xzSpread;
            double d4 = pos.getX() + d3 + random.nextDouble() * xzSpread * 2.0;
            double d5 = pos.getY() + random.nextDouble() * ySpread;
            double d6 = pos.getZ() + d3 + random.nextDouble() * xzSpread * 2.0;

            if (allowInAir || !level.getBlockState(BlockPos.containing(d4, d5, d6).below()).isAir()) {
                level.addParticle(particle, d4, d5, d6, d0, d1, d2);
            }
        }
    }
}

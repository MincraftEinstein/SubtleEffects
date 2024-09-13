package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BonemealableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "addGrowthParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ParticleUtils;spawnParticles(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;IDDZLnet/minecraft/core/particles/ParticleOptions;)V"))
    private static void spawnParticles(LevelAccessor accessor, BlockPos pos, int data, CallbackInfo ci) {
        if (ModConfigs.INSTANCE.boneMealUsingParticles.get()) {
            BlockPos particlePos = ((BonemealableBlock) accessor.getBlockState(pos).getBlock()).getParticlePos(pos);
            ParticleUtils.spawnParticles(accessor, particlePos, data, 3, 1, false, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.BONE_MEAL)));
        }
    }

    @Inject(method = "addGrowthParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ParticleUtils;spawnParticleInBlock(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;ILnet/minecraft/core/particles/ParticleOptions;)V"))
    private static void spawnParticlesInBlock(LevelAccessor accessor, BlockPos pos, int data, CallbackInfo ci) {
        if (ModConfigs.INSTANCE.boneMealUsingParticles.get()) {
            BlockPos particlePos = ((BonemealableBlock) accessor.getBlockState(pos).getBlock()).getParticlePos(pos);
            ParticleUtils.spawnParticleInBlock(accessor, particlePos, data / 3, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.BONE_MEAL)));
        }
    }
}

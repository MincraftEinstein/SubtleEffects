package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public class BeehiveBlockMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BeehiveBlock;dropHoneycomb(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (ModConfigs.INSTANCE.beehiveShearParticles.get()) {
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HONEYCOMB_BLOCK.defaultBlockState());
            for (int i = 0; i < 10; i++) {
                ParticleSpawnUtil.spawnParticlesOnSide(particle, 0.1F, state.getValue(BeehiveBlock.FACING), level, pos, player.getRandom(), 0, 0, 0);
            }
        }
    }
}

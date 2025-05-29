package einstein.subtle_effects.mixin.client.block.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VaultBlockEntity.Client.class)
public class VaultBlockEntityClientMixin {

    @ModifyExpressionValue(method = "emitConnectionParticlesForPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;VAULT_CONNECTION:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private static SimpleParticleType replaceOminousConnectionParticles(SimpleParticleType original, @Local(argsOnly = true) Level level, @Local(argsOnly = true) Vec3 connectionPos) {
        if (ModConfigs.BLOCKS.replaceOminousVaultConnection) {
            if (subtleEffects$findVault(level, connectionPos)) {
                return ModParticles.OMINOUS_VAULT_CONNECTION.get();
            }
        }
        return original;
    }

    @Unique
    private static boolean subtleEffects$findVault(Level level, Vec3 connectionPos) {
        BlockPos pos = BlockPos.containing(connectionPos);
        if (subtleEffects$isOminousVault(level, pos)) {
            return true;
        }

        for (Direction direction : Direction.values()) {
            BlockPos relativePos = pos.relative(direction);
            if (subtleEffects$isOminousVault(level, relativePos)) {
                return true;
            }
            else if (direction.getAxis().isHorizontal()) {
                return subtleEffects$isOminousVault(level, relativePos.below());
            }
        }
        return false;
    }

    @Unique
    private static boolean subtleEffects$isOminousVault(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.VAULT) && state.getValue(VaultBlock.OMINOUS);
    }
}

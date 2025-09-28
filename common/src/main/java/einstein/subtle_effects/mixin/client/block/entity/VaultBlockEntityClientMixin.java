package einstein.subtle_effects.mixin.client.block.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VaultBlockEntity.Client.class)
public class VaultBlockEntityClientMixin {

    @Unique
    private static final ThreadLocal<Boolean> IS_OMINOUS_VAULT = new ThreadLocal<>();

    @Inject(method = "emitConnectionParticlesForNearbyPlayers", at = @At("HEAD"))
    private static void checkAndSetOminousVault(Level level, BlockPos pos, BlockState state, VaultSharedData sharedData, CallbackInfo ci) {
        IS_OMINOUS_VAULT.set(state.is(Blocks.VAULT) && state.getValue(VaultBlock.OMINOUS));
    }

    @ModifyExpressionValue(method = "emitConnectionParticlesForPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;VAULT_CONNECTION:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private static SimpleParticleType replaceOminousConnectionParticles(SimpleParticleType original) {
        if (ModConfigs.BLOCKS.replaceOminousVaultConnection) {
            if (IS_OMINOUS_VAULT.get()) {
                return ModParticles.OMINOUS_VAULT_CONNECTION.get();
            }
        }
        return original;
    }
}

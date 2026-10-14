package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.PayloadSender;
import einstein.subtle_effects.networking.clientbound.ClientBoundItemEnchantedPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public class FabricEnchantmentMenuMixin {

    @Inject(method = "method_17410", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void spawnEnchantParticles(ItemStack stack, int id, Player player, int i, ItemStack lapisStack, Level level, BlockPos pos, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            PayloadSender.sendToClientsTracking(serverLevel, pos, new ClientBoundItemEnchantedPayload(pos));
        }
    }
}

package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
    private void spawnPickupEffects(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(ordinal = 0) BlockPos pos, @Local(ordinal = 1) ItemStack stack) {
        ParticleSpawnUtil.spawnBucketParticles(level, pos, stack);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BucketItem;checkExtraContent(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/BlockPos;)V"))
    private void spawnPlaceEffects(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(ordinal = 2) BlockPos pos, @Local(ordinal = 0) ItemStack stack) {
        ParticleSpawnUtil.spawnBucketParticles(level, pos, stack);
    }
}

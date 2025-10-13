package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public class BottleItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V"))
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local BlockPos pos) {
        if (level.isClientSide()) {
            ParticleSpawnUtil.spawnBucketParticles(level, pos, new ItemStack(Items.WATER_BUCKET));
        }
    }
}

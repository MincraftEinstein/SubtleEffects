package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SolidBucketItem.class)
public class SolidBucketItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/context/UseOnContext;getPlayer()Lnet/minecraft/world/entity/player/Player;"))
    private void spawnPlaceEffects(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local InteractionResult result) {
        Level level = context.getLevel();
        if (level.isClientSide && result.consumesAction()) {
            BlockPlaceContext blockContext = new BlockPlaceContext(context);
            BlockPos pos = blockContext.getClickedPos();
            ItemStack stack = blockContext.getItemInHand();

            if (stack.is(Items.POWDER_SNOW_BUCKET) && ModConfigs.ITEMS.powderSnowBucketUseParticles) {
                RandomSource random = level.getRandom();
                ParticleSpawnUtil.spawnBucketParticles(level, random, pos, ModParticles.SNOW.get(), random.nextDouble());
            }
        }
    }
}

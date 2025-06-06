package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BucketPickup;getPickupSound()Ljava/util/Optional;"))
    private void spawnPickupEffects(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(ordinal = 0) BlockPos pos, @Local(ordinal = 1) ItemStack stack) {
        subtleEffects$spawnBucketParticles(level, pos, stack);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BucketItem;checkExtraContent(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/BlockPos;)V"))
    private void spawnPlaceEffects(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(ordinal = 2) BlockPos pos, @Local(ordinal = 0) ItemStack stack) {
        subtleEffects$spawnBucketParticles(level, pos, stack);
    }

    @Unique
    private void subtleEffects$spawnBucketParticles(Level level, BlockPos pos, ItemStack stack) {
        if (level.isClientSide) {
            if (stack.getItem() instanceof BucketItemAccessor bucket) {
                Fluid content = bucket.getContent();
                boolean isWater = content.isSame(Fluids.WATER);

                if ((isWater && ModConfigs.ITEMS.waterBucketUseParticles) || (content.isSame(Fluids.LAVA) && ModConfigs.ITEMS.lavaBucketUseParticles)) {
                    if (isWater && level.dimensionType().ultraWarm()) {
                        return;
                    }

                    ParticleSpawnUtil.spawnBucketParticles(level, pos, Util.getParticleForFluid(content));
                }
            }
            else if (stack.is(Items.POWDER_SNOW_BUCKET) && ModConfigs.ITEMS.powderSnowBucketUseParticles) {
                ParticleSpawnUtil.spawnBucketParticles(level, pos, ModParticles.SNOW.get());
            }
        }
    }

    @WrapOperation(method = "emptyContents*", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke(Operation<SimpleParticleType> original) {
        if (ModConfigs.ITEMS.waterEvaporateFromBucketSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }
}

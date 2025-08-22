package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.networking.clientbound.ClientBoundAnimalFedPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public class MobMixin {

    @WrapOperation(method = "spawnAnim", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getX(D)D"))
    private double getX(Mob mob, double scale, Operation<Double> original) {
        return mob.getRandomX(scale);
    }

    @WrapOperation(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult onMobInteract(Mob mob, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        ItemStack stack = player.getItemInHand(hand).copy();
        InteractionResult result = original.call(mob, player, hand);
        Level level = mob.level();

        if (level instanceof ServerLevel serverLevel && result.consumesAction()) {
            if (subtleEffects$isFood(mob, stack)) {
                ItemStack stackCopy = stack.copy();
                ItemStack handStack = player.getMainHandItem().copy();
                stack.shrink(1);

                if (!stackCopy.isEmpty()) {
                    if ((ItemStack.isSameItemSameComponents(stack, handStack) && stack.getCount() == handStack.getCount())
                            || player.isCreative()) {
                        Services.NETWORK.sendToClientsTracking(serverLevel, mob.blockPosition(), new ClientBoundAnimalFedPayload(mob.getId(), stack.isEmpty() ? stackCopy : stack));
                    }
                }
            }
        }
        return result;
    }

    @Unique
    private static boolean subtleEffects$isFood(Mob mob, ItemStack stack) {
        if (mob instanceof Animal animal) {
            return animal.isFood(stack)
                    || (animal instanceof Wolf && stack.is(Items.BONE))
                    || (animal instanceof Parrot && (stack.is(ItemTags.PARROT_FOOD) || stack.is(ItemTags.PARROT_POISONOUS_FOOD)));
        }
        else if (mob instanceof Dolphin) {
            return stack.is(ItemTags.FISHES);
        }
        else if (mob instanceof Tadpole) {
            return stack.is(ItemTags.FROG_FOOD);
        }
        return false;
    }
}
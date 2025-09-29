package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.clientbound.ClientBoundCompostItemPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorkAtComposter.class)
public class WorkAtComposterMixin {

    @Shadow
    @Final
    private static List<Item> COMPOSTABLE_ITEMS;

    @Inject(method = "compostItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/WorkAtComposter;spawnComposterFillEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 0))
    private void spawnComposterEffects(ServerLevel level, Villager villager, GlobalPos global, BlockState state, CallbackInfo ci, @Local ItemStack stack) {
        BlockPos pos = global.pos();
        Services.NETWORK.sendToClientsTracking(level, pos, new ClientBoundCompostItemPayload(stack, pos, true));
    }

    @Inject(method = "compostItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/WorkAtComposter;spawnComposterFillEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 1))
    private void spawnComposterEffects(ServerLevel level, Villager villager, GlobalPos global, BlockState state, CallbackInfo ci, @Local SimpleContainer inventory) {
        BlockPos pos = global.pos();
        List<ItemStack> compostableItems = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && stack.getCount() > 10 && COMPOSTABLE_ITEMS.contains(stack.getItem())) {
                compostableItems.add(stack);
            }
        }

        ItemStack stack = compostableItems.isEmpty() ? new ItemStack(Items.WHEAT_SEEDS) : compostableItems.get(level.getRandom().nextInt(compostableItems.size())).copy();
        if (!stack.isEmpty()) {
            Services.NETWORK.sendToClientsTracking(level, pos, new ClientBoundCompostItemPayload(stack, pos, true));
        }
    }
}

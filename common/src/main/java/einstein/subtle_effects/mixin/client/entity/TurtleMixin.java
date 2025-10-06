package com.mincrafteinstein.subtleeffects.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Turtle.class)
public abstract class TurtleMixin {

    // Use persistent data key to prevent infinite brushing
    private static final String LAST_BRUSHED_KEY = "subtleeffects.last_brushed";

    @Shadow
    public abstract Level level();

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void subtleeffects$dropScuteOnBrush(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = level();
        ItemStack held = player.getItemInHand(hand);
        Item item = held.getItem();

        // Only trigger with vanilla archaeology brush
        if (item != Items.BRUSH) return;

        Turtle turtle = (Turtle) (Object) this;

        // Skip babies — only adult turtles
        if (turtle.isBaby()) return;

        // Cooldown check
        long now = world.getGameTime();
        long last = turtle.getPersistentData().getLong(LAST_BRUSHED_KEY);
        if (now - last < 200) { // 10 seconds (200 ticks)
            return;
        }

        // Server-side logic
        if (!world.isClientSide) {
            ServerLevel server = (ServerLevel) world;

            // Drop one turtle scute
            ItemEntity scuteDrop = new ItemEntity(server,
                    turtle.getX(),
                    turtle.getY() + 0.5,
                    turtle.getZ(),
                    new ItemStack(Items.SCUTE));
            server.addFreshEntity(scuteDrop);

            // Mark last brushed time
            turtle.getPersistentData().putLong(LAST_BRUSHED_KEY, now);

            // Optional: damage brush slightly
            held.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));

            // Optionally: play particles or sound for feedback
            server.levelEvent(2001, turtle.blockPosition(), Item.getId(Items.SCUTE.getDefaultInstance().getItem())); // block break particles

            // Consume interaction
            cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }
}

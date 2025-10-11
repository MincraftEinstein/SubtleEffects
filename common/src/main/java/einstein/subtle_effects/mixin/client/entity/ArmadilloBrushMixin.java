package com.mincrafteinstein.subtle_effects.mixin.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Armadillo.class)
public class ArmadilloBrushMixin {

    @Inject(method = "interact", at = @At("HEAD"))
    private void subtle_effects$spawnBrushFlakes(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!player.level().isClientSide()) return;

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) return;

        String id = stack.getItem().builtInRegistryHolder().key().location().getPath();
        if (!id.contains("brush")) return;

        Armadillo armadillo = (Armadillo) (Object) this;
        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null) return;

        double cx = armadillo.getX();
        double cy = armadillo.getY() + armadillo.getBbHeight() * 0.5;
        double cz = armadillo.getZ();

        for (int i = 0; i < 10; i++) {
            double ox = (level.random.nextDouble() - 0.5) * armadillo.getBbWidth() * 1.5;
            double oy = level.random.nextDouble() * armadillo.getBbHeight() * 0.8;
            double oz = (level.random.nextDouble() - 0.5) * armadillo.getBbWidth() * 1.5;
            double vx = (level.random.nextDouble() - 0.5) * 0.02;
            double vy = 0.05 + level.random.nextDouble() * 0.03;
            double vz = (level.random.nextDouble() - 0.5) * 0.02;

            level.addParticle(ParticleTypes.WHITE_ASH, cx + ox, cy + oy, cz + oz, vx, vy, vz);
        }
    }
}

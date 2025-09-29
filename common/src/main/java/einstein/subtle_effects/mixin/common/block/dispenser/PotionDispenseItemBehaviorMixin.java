package einstein.subtle_effects.mixin.common.block.dispenser;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.networking.clientbound.ClientBoundDispenseBucketPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$15")
public class PotionDispenseItemBehaviorMixin {

    @WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private <T extends ParticleOptions> int cancelVanillaParticles(ServerLevel level, T particle, double x, double y, double z, int count, double xOffset, double yOffset, double zOffset, double speed, Operation<Integer> original) {
        return -1;
    }

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V"))
    private void execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ServerLevel level = source.level();
        BlockPos pos = source.pos();

        level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS); // Fixes inconsistency with Item.useOn behavior vs dispenser behavior. (there's probably a bug report for this, but I'm too lazy to find it)
        Services.NETWORK.sendToClientsTracking(null, level, pos, new ClientBoundDispenseBucketPayload(new ItemStack(Items.WATER_BUCKET), pos), (serverPlayer) -> {
            RandomSource random = level.getRandom();

            for (int i = 0; i < 5; i++) {
                level.sendParticles(serverPlayer, ParticleTypes.SPLASH, false, false, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 1, 0, 0, 0, 1);
            }
        });
    }
}

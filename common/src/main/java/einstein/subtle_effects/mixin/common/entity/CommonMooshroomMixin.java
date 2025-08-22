package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.networking.clientbound.ClientBoundMooshroomShearedPayload;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MushroomCow.class)
public class CommonMooshroomMixin {

    @Unique
    private final MushroomCow subtleEffects$me = (MushroomCow) (Object) this;

    @WrapOperation(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private <T extends ParticleOptions> int replaceShearingParticles(ServerLevel level, T particle, double x, double y, double z, int count, double xOffset, double yOffset, double zOffset, double speed, Operation<Integer> original) {
        Services.NETWORK.sendToClientsTracking(null, level, BlockPos.containing(x, y, z), new ClientBoundMooshroomShearedPayload(subtleEffects$me.getId()), serverPlayer ->
                level.sendParticles(serverPlayer, particle, false, x, y, z, count, xOffset, yOffset, zOffset, speed)
        );
        return -1;
    }
}

package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.option.SplashEmitterParticleOptions;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow
    private ClientLevel level;

    @Inject(method = "handleExplosion", at = @At("TAIL"))
    private void handleExplosion(ClientboundExplodePacket packet, CallbackInfo ci) {
        if (ModConfigs.ENTITIES.splashes.splashEffects.get() && ModConfigs.ENTITIES.splashes.explosionsCauseSplashes.get()) {
            float radius = packet.radius();
            Vec3 position = packet.center();
            BlockPos pos = BlockPos.containing(position);
            FluidState fluidState = level.getFluidState(pos);

            if (!fluidState.isEmpty()) {
                int blockY = pos.getY();

                for (int y = blockY; y < blockY + (radius * 1.5) + 1; y++) {
                    BlockPos currentPos = pos.atY(y);
                    FluidState currentFluidState = level.getFluidState(currentPos);

                    if (fluidState.getType().isSame(currentFluidState.getType())) {
                        continue;
                    }

                    if (level.getBlockState(currentPos).isSolidRender()) {
                        return;
                    }

                    FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
                    if (fluidDefinition != null) {
                        if (fluidDefinition.splashType().isPresent()) {
                            BlockPos surfacePos = currentPos.below();
                            FluidState surfaceFluidState = level.getFluidState(surfacePos);
                            float scale = radius - ((y - blockY) / radius);

                            level.addAlwaysVisibleParticle(new SplashEmitterParticleOptions(fluidDefinition.id(), scale, scale * (scale * 0.1F), -1, -1),
                                    true, position.x(), surfacePos.getY() + surfaceFluidState.getHeight(level, surfacePos) + 0.01, position.z(),
                                    0, 0, 0
                            );
                        }
                    }

                    return;
                }
            }
        }
    }
}

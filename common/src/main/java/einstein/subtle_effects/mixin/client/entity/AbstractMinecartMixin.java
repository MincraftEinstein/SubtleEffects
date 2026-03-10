package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.util.FluidLogicAccessor;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {

    @Unique
    private final AbstractMinecart subtleEffects$me = (AbstractMinecart) (Object) this;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;handlePortal()V"))
    private void tick(CallbackInfo ci) {
        FluidLogicAccessor.clientUpdateInWaterStateAndDoFluidPushing(subtleEffects$me);
    }
}

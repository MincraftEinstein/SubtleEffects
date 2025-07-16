package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Mob.class)
public class MobMixin {

    @WrapOperation(method = "spawnAnim", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getX(D)D"))
    private double getX(Mob mob, double scale, Operation<Double> original) {
        return mob.getRandomX(scale);
    }
}

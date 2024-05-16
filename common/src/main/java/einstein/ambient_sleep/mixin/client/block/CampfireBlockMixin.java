package einstein.ambient_sleep.mixin.client.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.ambient_sleep.init.ModConfigs;
import net.minecraft.world.level.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @ModifyExpressionValue(method = "animateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/CampfireBlock;spawnParticles:Z"))
    private boolean shouldDisableLavaSparks(boolean original) {
        return original && !ModConfigs.INSTANCE.removeVanillaCampfireSparks.get();
    }
}

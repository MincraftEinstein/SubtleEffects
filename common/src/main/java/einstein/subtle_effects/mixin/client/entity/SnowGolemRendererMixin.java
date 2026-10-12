package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.HolidayHelper;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SnowGolemRenderer.class)
public class SnowGolemRendererMixin {

    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/world/entity/animal/golem/SnowGolem;Lnet/minecraft/client/renderer/entity/state/SnowGolemRenderState;F)V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;CARVED_PUMPKIN:Lnet/minecraft/world/level/block/Block;"))
    private Block replacePumpkin(Block original) {
        return HolidayHelper.HALLOWEEN.get() && ModConfigs.GENERAL.enableEasterEggs ? Blocks.JACK_O_LANTERN : original;
    }
}

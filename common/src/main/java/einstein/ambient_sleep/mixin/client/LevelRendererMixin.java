package einstein.ambient_sleep.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(method = "levelEvent", at = @At("TAIL"))
    private void levelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        if (level == null) {
            return;
        }

        if (type == 1029) {
            level.addDestroyBlockEffect(pos, Blocks.ANVIL.defaultBlockState());
        }
    }
}

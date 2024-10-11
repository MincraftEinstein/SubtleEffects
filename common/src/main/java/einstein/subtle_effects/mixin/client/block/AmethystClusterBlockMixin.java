package einstein.subtle_effects.mixin.client.block;

import einstein.subtle_effects.util.AmethystClusterBlockAccessor;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AmethystClusterBlock.class)
public class AmethystClusterBlockMixin implements AmethystClusterBlockAccessor {

    @Unique
    private int subtleEffects$height;
    @Unique
    private int subtleEffects$aabbOffset;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(int height, int aabbOffset, BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.subtleEffects$height = height;
        this.subtleEffects$aabbOffset = aabbOffset;
    }

    @Override
    public float subtleEffects$getHeight() {
        return subtleEffects$height;
    }

    @Override
    public float subtleEffects$getAABBOffset() {
        return subtleEffects$aabbOffset;
    }
}

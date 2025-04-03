package einstein.subtle_effects.mixin.client.block;

import net.minecraft.world.level.block.AmethystClusterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AmethystClusterBlock.class)
public interface AmethystClusterBlockAccessor {

    @Accessor("height")
    float getHeight();

    @Accessor("width")
    float getAABBOffset();
}

package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.util.BucketItemAccessor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BucketItem.class)
public class FabricBucketItemAccessor implements BucketItemAccessor {

    @Shadow
    @Final
    private Fluid content;

    @Override
    public Fluid getContent() {
        return content;
    }
}

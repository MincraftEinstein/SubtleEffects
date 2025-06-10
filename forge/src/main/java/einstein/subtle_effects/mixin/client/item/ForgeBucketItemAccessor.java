package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.util.BucketItemAccessor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(BucketItem.class)
public class ForgeBucketItemAccessor implements BucketItemAccessor {

    @Shadow
    @Final
    private Supplier<? extends Fluid> fluidSupplier;

    @Override
    public Fluid getContent() {
        return fluidSupplier.get();
    }
}

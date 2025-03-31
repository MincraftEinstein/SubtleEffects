package einstein.subtle_effects.mixin.client.entity;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Invoker("doWaterSplashEffect")
    void doWaterSplashingEffects();
}

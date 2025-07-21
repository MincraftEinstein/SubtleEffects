package einstein.subtle_effects.mixin.client.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractHorse.class)
public interface AbstractHorseAccessor {

    @Nullable
    @Invoker("getEatingSound")
    SoundEvent getEatSound();
}

package einstein.ambient_sleep.mixin.client.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("tickCount")
    int getTickCount();

    @Accessor("random")
    RandomSource getRandom();
}

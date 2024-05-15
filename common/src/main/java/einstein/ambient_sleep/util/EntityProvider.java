package einstein.ambient_sleep.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityProvider<T extends Entity> {

    void apply(T entity, Level level, RandomSource random);
}

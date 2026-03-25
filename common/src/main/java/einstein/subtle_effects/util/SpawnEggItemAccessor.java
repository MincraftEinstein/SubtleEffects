package einstein.subtle_effects.util;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public interface SpawnEggItemAccessor {

    @Nullable
    EntityType<?> subtleEffects$getDefaultEntityType();
}

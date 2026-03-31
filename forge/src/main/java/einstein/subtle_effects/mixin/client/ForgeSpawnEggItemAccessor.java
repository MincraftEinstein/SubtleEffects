package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.util.SpawnEggItemAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = SpawnEggItem.class, priority = 1001)
public abstract class ForgeSpawnEggItemAccessor implements SpawnEggItemAccessor {

    @Override
    @Nullable
    @Invoker("getDefaultType")
    public abstract EntityType<?> subtleEffects$getDefaultEntityType();
}

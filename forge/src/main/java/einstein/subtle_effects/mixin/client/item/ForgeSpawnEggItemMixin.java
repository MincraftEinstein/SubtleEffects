package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.util.SpawnEggItemAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForgeSpawnEggItem.class)
public abstract class ForgeSpawnEggItemMixin implements SpawnEggItemAccessor {

    @Shadow
    protected abstract EntityType<?> getDefaultType();

    @Override
    public @Nullable EntityType<?> subtleEffects$getDefaultEntityType() {
        return getDefaultType();
    }
}

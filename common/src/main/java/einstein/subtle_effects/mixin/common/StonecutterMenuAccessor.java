package einstein.subtle_effects.mixin.common;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StonecutterMenu.class)
public interface StonecutterMenuAccessor {

    @Accessor("inputSlot")
    Slot getInputSlot();
}

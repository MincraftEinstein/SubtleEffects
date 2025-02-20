package einstein.subtle_effects.mixin.client;

import com.anthonyhilyard.itemborders.config.ItemBordersConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = ItemBordersConfig.class, remap = false)
public interface ItemBordersConfigAccessor {

    @Accessor("manualBorders")
    Supplier<Map<String, Object>> getManualBorders();
}

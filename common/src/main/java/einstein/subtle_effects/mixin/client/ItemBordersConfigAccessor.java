package einstein.subtle_effects.mixin.client;

import com.anthonyhilyard.itemborders.config.ItemBordersConfig;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import einstein.subtle_effects.compat.CompatHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Supplier;

@IfModLoaded(CompatHelper.ITEMBORDERS_MOD_ID)
@Mixin(value = ItemBordersConfig.class, remap = false)
public interface ItemBordersConfigAccessor {

    @Accessor("manualBorders")
    Supplier<Map<String, Object>> getManualBorders();
}

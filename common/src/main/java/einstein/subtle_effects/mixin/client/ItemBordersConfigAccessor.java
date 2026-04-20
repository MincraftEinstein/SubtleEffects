package einstein.subtle_effects.mixin.client;

import com.anthonyhilyard.itemborders.ItemBordersConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import einstein.subtle_effects.compat.CompatHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded(CompatHelper.ITEMBORDERS_MOD_ID)
@Mixin(value = ItemBordersConfig.class, remap = false)
public interface ItemBordersConfigAccessor {

    @Accessor("manualBorders")
    ForgeConfigSpec.ConfigValue<UnmodifiableConfig> getManualBorders();

    @Accessor("INSTANCE")
    static ItemBordersConfig getInstance() {
        throw new AssertionError();
    }
}

package einstein.ambient_sleep.init;

import einstein.ambient_sleep.AmbientSleep;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {

    private static final Pair<ModConfigs, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    public static final ModConfigs INSTANCE = SPEC_PAIR.getLeft();
    public static final ForgeConfigSpec SPEC = SPEC_PAIR.getRight();

    public ModConfigs(ForgeConfigSpec.Builder builder) {
        builder.push("Blocks");

        builder.pop().push("Entities");
    }

    private static String key(String path) {
        return "config." + AmbientSleep.MOD_ID + "." + path;
    }
}

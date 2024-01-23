package einstein.ambient_sleep.init;

import einstein.ambient_sleep.AmbientSleep;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {

    private static final Pair<ModConfigs, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    public static final ModConfigs INSTANCE = SPEC_PAIR.getLeft();
    public static final ForgeConfigSpec SPEC = SPEC_PAIR.getRight();

    public final ForgeConfigSpec.BooleanValue disableDefaultCampfireSparks;

    public ModConfigs(ForgeConfigSpec.Builder builder) {
        builder.push("Blocks");

        disableDefaultCampfireSparks = builder
                .comment("Disables the default lava spark particle from campfires")
                .translation(key("disable_default_campfire_sparks"))
                .define("disableDefaultCampfireSparks", true);

        builder.pop().push("Entities");
    }

    private static String key(String path) {
        return "config." + AmbientSleep.MOD_ID + "." + path;
    }
}

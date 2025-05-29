package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.*;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;

import java.util.function.Supplier;

public class ModConfigs {

    public static final String BASE_KEY = "config." + SubtleEffects.MOD_ID + ".";

    public static final ModGeneralConfigs GENERAL = register(ModGeneralConfigs::new);
    public static final ModBlockConfigs BLOCKS = register(ModBlockConfigs::new);
    public static final ModEntityConfigs ENTITIES = register(ModEntityConfigs::new);
    public static final ModEnvironmentConfigs ENVIRONMENT = register(ModEnvironmentConfigs::new);
    public static final ModItemConfigs ITEMS = register(ModItemConfigs::new);

    public static void init() {
    }

    private static <T extends Config> T register(Supplier<T> supplier) {
        return ConfigApiJava.registerAndLoadConfig(supplier, RegisterType.CLIENT);
    }
}

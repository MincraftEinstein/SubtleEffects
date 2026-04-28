package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.SuppliedComponent;
import einstein.subtle_effects.configs.*;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditional(T config, ValidatedField<Integer> dependencyConfig) {
        return conditional(config, () -> dependencyConfig.get() > 0, dependencyConfig);
    }

    public static <T extends ValidatedField<V>, V, M extends Enum<?>> ValidatedCondition<V> conditional(T config, ValidatedField<M> dependencyConfig, Object... disabled) {
        if (disabled.length == 0) {
            throw new IllegalArgumentException();
        }
        return conditional(config, () -> (disabled.length == 1 ? dependencyConfig.get() != disabled[0] : Arrays.stream(disabled).allMatch(object -> dependencyConfig.get() != object)), dependencyConfig);
    }

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditional(T config, ValidatedBoolean dependencyConfig) {
        return conditional(config, dependencyConfig, dependencyConfig);
    }

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditional(T config, Supplier<Boolean> condition, Translatable dependencyConfig) {
        return conditional(config, condition, () -> dependencyConfig);
    }

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditional(T config, Supplier<Boolean> condition, Supplier<Translatable> dependencyConfig) {
        return conditional(config, condition, dependencyConfig, true);
    }

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditional(T config, Supplier<Boolean> condition, Supplier<Translatable> dependencyConfig, boolean memoize) {
        return config.toCondition(condition, createFailMessage(dependencyConfig, memoize), config::getDefault);
    }

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditional(T config, String modId) {
        return config.toCondition(() -> Services.PLATFORM.isModLoaded(modId), new SuppliedComponent(true,
                () -> styleFailMessage(Component.translatable("config.subtle_effects.conditions.mod_not_loaded", modId))
        ), config::getDefault);
    }

    public static <T extends ValidatedField<V>, V> ValidatedCondition<V> conditionalModLoaded(T config, String modId) {
        return config.toCondition(() -> !Services.PLATFORM.isModLoaded(modId), new SuppliedComponent(true,
                () -> styleFailMessage(Component.translatable("config.subtle_effects.conditions.mod_loaded", modId))
        ), config::getDefault);
    }

    public static SuppliedComponent createFailMessage(Supplier<Translatable> dependencyConfig, boolean memoize) {
        return new SuppliedComponent(memoize,
                () -> {
                    Translatable translatable = dependencyConfig.get();
                    return styleFailMessage(
                            Component.translatable("config.subtle_effects.conditions.config_disabled",
                            translatable.translation(translatable.translationKey()))
                    );
                }
        );
    }

    private static Component styleFailMessage(MutableComponent component) {
        return component.withStyle(style -> style.withColor(ChatFormatting.RED));
    }

    public static ValidatedList<Identifier> biomeList(String... biomeIds) {
        return registryList(Registries.BIOME, biomeIds);
    }

    public static <T> ValidatedList<Identifier> registryList(ResourceKey<? extends Registry<? extends T>> registryKey, Collection<Identifier> defaultIds) {
        return registryList(registryKey, defaultIds.stream().map(Identifier::toString).toArray(String[]::new));
    }

    public static <T> ValidatedList<Identifier> registryList(ResourceKey<? extends Registry<? extends T>> registryKey, String... defaultIds) {
        return new ValidatedIdentifier(Identifier.withDefaultNamespace("air"),
                        new AllowableIdentifiers(
                                location -> getRegistry(registryKey).map(biomes -> biomes.containsKey(location)).orElse(true),
                                () -> getRegistry(registryKey).map(biomes -> biomes.keySet().stream().toList()).orElseGet(List::of)
                        )
                ).toList(Arrays.stream(defaultIds).map(Identifier::tryParse).toList());
    }

    private static <T> Optional<Registry<T>> getRegistry(ResourceKey<? extends Registry<? extends T>> registryKey) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            return level.registryAccess().lookup(registryKey);
        }
        return Optional.empty();
    }
}

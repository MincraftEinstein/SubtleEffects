package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.*;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

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

    public static ValidatedList<ResourceLocation> biomeList(String... biomeIds) {
        return registryList(Registries.BIOME, biomeIds);
    }

    public static <T> ValidatedList<ResourceLocation> registryList(ResourceKey<? extends Registry<? extends T>> registryKey, Collection<ResourceLocation> defaultIds) {
        return registryList(registryKey, defaultIds.stream().map(ResourceLocation::toString).toArray(String[]::new));
    }

    public static <T> ValidatedList<ResourceLocation> registryList(ResourceKey<? extends Registry<? extends T>> registryKey, String... defaultIds) {
        return new ValidatedList<>(Arrays.stream(defaultIds).map(ResourceLocation::tryParse).toList(),
                new ValidatedIdentifier(new ResourceLocation("air"),
                        new AllowableIdentifiers(
                                location -> getRegistry(registryKey).map(biomes -> biomes.containsKey(location)).orElse(true),
                                () -> getRegistry(registryKey).map(biomes -> biomes.keySet().stream().toList()).orElseGet(List::of)
                        )
                )
        );
    }

    private static <T> Optional<Registry<T>> getRegistry(ResourceKey<? extends Registry<? extends T>> registryKey) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            return level.registryAccess().registry(registryKey);
        }
        return Optional.empty();
    }
}

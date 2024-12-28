package einstein.subtle_effects.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class SparkProviderReloadListener extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "subtle_effects/spark_providers";
    public static final Gson GSON = new GsonBuilder().create();
    public static final Map<ResourceLocation, SparkProvider> SPARK_PROVIDERS = new HashMap<>();

    public SparkProviderReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller filler) {
        SPARK_PROVIDERS.clear();

        resources.forEach((id, element) -> {
            SparkProvider.CODEC.parse(JsonOps.INSTANCE, element)
                    .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode spark provider with ID {} - Error: {}", id, error))
                    .ifPresent(provider -> provider.options().ifPresent(options -> SPARK_PROVIDERS.put(id, provider)));
        });
    }
}

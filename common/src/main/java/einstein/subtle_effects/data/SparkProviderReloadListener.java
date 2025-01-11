package einstein.subtle_effects.data;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class SparkProviderReloadListener extends SimpleJsonResourceReloadListener<SparkProvider> {

    public static final FileToIdConverter LISTER = FileToIdConverter.json("subtle_effects/spark_providers");
    public static final Map<ResourceLocation, SparkProvider> SPARK_PROVIDERS = new HashMap<>();

    public SparkProviderReloadListener() {
        super(SparkProvider.CODEC, LISTER);
    }

    @Override
    protected void apply(Map<ResourceLocation, SparkProvider> resources, ResourceManager manager, ProfilerFiller filler) {
        SPARK_PROVIDERS.clear();

        resources.forEach((id, provider) ->
                provider.options().ifPresent(options -> SPARK_PROVIDERS.put(id, provider))
        );
    }
}

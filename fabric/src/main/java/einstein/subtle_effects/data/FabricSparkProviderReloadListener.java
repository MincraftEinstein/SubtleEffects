package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricSparkProviderReloadListener extends SparkProviderReloadListener implements IdentifiableResourceReloadListener {

    public FabricSparkProviderReloadListener() {
        super();
    }

    @Override
    public ResourceLocation getFabricId() {
        return SubtleEffects.loc("spark_providers");
    }
}

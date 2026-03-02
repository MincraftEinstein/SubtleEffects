package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class BurningEffectsReloadListener extends SimpleJsonResourceReloadListener implements NamedReloadListener {

    private static final String DIRECTORY = "subtle_effects/burning_effects";
    public static final Map<ResourceLocation, BurningEffects> PROMETHEUS_BURNING_EFFECTS = new HashMap<>();
    public static final Map<ResourceLocation, BurningEffects> DYED_FLAMES_BURNING_EFFECTS = new HashMap<>();

    public BurningEffectsReloadListener() {
        super(Util.GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        PROMETHEUS_BURNING_EFFECTS.clear();

        resources.forEach((id, element) ->
                BurningEffects.Data.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode burning effects with ID {} - Error: {}", id, error))
                        .ifPresent(BurningEffectsReloadListener::load)
        );
    }

    private static void load(BurningEffects.Data data) {
        ResourceLocation id = data.id();
        BurningEffects burningEffects = new BurningEffects(data.colorProvider(), data.flameParticle());

        if (data.isPrometheus()) {
            PROMETHEUS_BURNING_EFFECTS.put(id, burningEffects);
            return;
        }

        DYED_FLAMES_BURNING_EFFECTS.put(id, burningEffects);
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("burning_effects");
    }
}


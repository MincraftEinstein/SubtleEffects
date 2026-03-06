package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class BurningEffectsReloadListener extends SimpleJsonResourceReloadListener<BurningEffects.Data> implements NamedReloadListener {

    private static final FileToIdConverter DIRECTORY = FileToIdConverter.json("subtle_effects/burning_effects");
    public static final Map<Identifier, BurningEffects> PROMETHEUS_BURNING_EFFECTS = new HashMap<>();
    public static final Map<Identifier, BurningEffects> DYED_FLAMES_BURNING_EFFECTS = new HashMap<>();

    public BurningEffectsReloadListener() {
        super(BurningEffects.Data.CODEC, DIRECTORY);
    }

    @Override
    protected void apply(Map<Identifier, BurningEffects.Data> resources, ResourceManager manager, ProfilerFiller profiler) {
        PROMETHEUS_BURNING_EFFECTS.clear();
        resources.forEach((id, data) -> load(data));
    }

    private static void load(BurningEffects.Data data) {
        Identifier id = data.id();
        BurningEffects burningEffects = new BurningEffects(data.colorProvider(), data.flameParticle());

        if (data.isPrometheus()) {
            PROMETHEUS_BURNING_EFFECTS.put(id, burningEffects);
            return;
        }

        DYED_FLAMES_BURNING_EFFECTS.put(id, burningEffects);
    }

    @Override
    public Identifier getId() {
        return SubtleEffects.loc("burning_effects");
    }
}


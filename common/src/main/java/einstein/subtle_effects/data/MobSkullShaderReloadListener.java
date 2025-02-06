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

public class MobSkullShaderReloadListener extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "subtle_effects/mob_skull_shaders";
    public static final Map<ResourceLocation, MobSkullShader> MOB_SKULL_SHADERS = new HashMap<>();

    public MobSkullShaderReloadListener() {
        super(Util.GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        MOB_SKULL_SHADERS.clear();

        resources.forEach((id, element) -> {
            MobSkullShader.CODEC.parse(JsonOps.INSTANCE, element)
                    .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode mob skull shader with ID {} - Error: {}", id, error))
                    .ifPresent(mobSkullShader -> {
                        ResourceLocation shaderId = mobSkullShader.shaderId();
                        manager.getResource(shaderId).ifPresentOrElse(
                                resource -> MOB_SKULL_SHADERS.put(id, mobSkullShader),
                                () -> SubtleEffects.LOGGER.info("Invalid shader {} for mob skull shader with ID {}", shaderId, id)
                        );
                    });
        });
    }
}

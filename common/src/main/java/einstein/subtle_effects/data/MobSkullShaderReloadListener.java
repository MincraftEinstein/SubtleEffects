package einstein.subtle_effects.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class MobSkullShaderReloadListener extends SimpleJsonResourceReloadListener implements NamedReloadListener{

    public static final String DIRECTORY = "subtle_effects/mob_skull_shaders";
    public static final Map<ResourceLocation, MobSkullShaderData> MOB_SKULL_SHADERS = new HashMap<>();

    public MobSkullShaderReloadListener() {
        super(Util.GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, MobSkullShaderData> dataMap = new HashMap<>();
        MOB_SKULL_SHADERS.clear();

        resources.forEach((id, element) -> {
            MobSkullShaderData.CODEC.parse(JsonOps.INSTANCE, element)
                    .resultOrPartial(error -> SubtleEffects.LOGGER.error("Failed to decode mob skull shader with ID {} - Error: {}", id, error))
                    .ifPresent(shaderData -> dataMap.put(id, shaderData));
        });

        load(manager, dataMap);
    }

    private static void load(ResourceManager manager, Map<ResourceLocation, MobSkullShaderData> dataMap) {
        dataMap.forEach((location, shaderData) -> {
            Item item = shaderData.stackHolder().item();
            if (item.equals(Items.AIR)) {
                SubtleEffects.LOGGER.error("Item in Mob Skull Shader '{}' can not be air", location);
                return;
            }

            ResourceLocation shaderId = shaderData.shaderId();
            if (manager.getResource(shaderId).isEmpty()) {
                SubtleEffects.LOGGER.error("Could not find post shader with ID '{}' for Mob Skull Shader: '{}'", shaderId, location);
                return;
            }

            MOB_SKULL_SHADERS.put(location, shaderData);
        });
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("mob_skull_shaders");
    }
}

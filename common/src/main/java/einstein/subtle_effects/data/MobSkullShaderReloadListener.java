package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class MobSkullShaderReloadListener extends SimpleJsonResourceReloadListener<MobSkullShaderData> implements NamedReloadListener {

    public static final FileToIdConverter DIRECTORY = FileToIdConverter.json("subtle_effects/mob_skull_shaders");
    public static final Map<Identifier, MobSkullShaderData> MOB_SKULL_SHADERS = new HashMap<>();

    public MobSkullShaderReloadListener() {
        super(MobSkullShaderData.CODEC, DIRECTORY);
    }

    @Override
    protected void apply(Map<Identifier, MobSkullShaderData> resources, ResourceManager manager, ProfilerFiller profiler) {
        MOB_SKULL_SHADERS.clear();
        load(manager, resources);
    }

    private static void load(ResourceManager manager, Map<Identifier, MobSkullShaderData> resources) {
        resources.forEach((location, shaderData) -> {
            Item item = shaderData.stackHolder().item();
            if (item.equals(Items.AIR)) {
                SubtleEffects.LOGGER.error("Item in Mob Skull Shader '{}' can not be air", location);
                return;
            }

            Identifier shaderId = shaderData.shaderId();
            if (manager.getResource(shaderId.withPath("post_effect/" + shaderId.getPath() + ".json")).isEmpty()) {
                SubtleEffects.LOGGER.error("Could not find post shader with ID '{}' for Mob Skull Shader: '{}'", shaderId, location);
                return;
            }

            MOB_SKULL_SHADERS.put(location, shaderData);
        });
    }

    @Override
    public Identifier getId() {
        return SubtleEffects.loc("mob_skull_shaders");
    }
}

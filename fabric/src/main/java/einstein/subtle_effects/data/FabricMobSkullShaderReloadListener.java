package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricMobSkullShaderReloadListener extends MobSkullShaderReloadListener implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return SubtleEffects.loc("mob_skull_shaders");
    }
}

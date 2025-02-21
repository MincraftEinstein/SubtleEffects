package einstein.subtle_effects.data;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricMobSkullShaderReloadListener extends MobSkullShaderReloadListener implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }
}

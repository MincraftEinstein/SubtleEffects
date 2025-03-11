package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricBCWPPackManager extends BCWPPackManager implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return SubtleEffects.loc("biome_color_water_particles_pack_manager");
    }
}

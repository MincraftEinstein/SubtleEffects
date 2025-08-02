package einstein.subtle_effects.client.renderer.entity;

import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;

public class EinsteinSolarSystemRenderLayerParentImpl<T extends AbstractClientPlayer, V extends PlayerModel<T>> implements RenderLayerParent<T, EinsteinSolarSystemModel<T>> {

    private final EinsteinSolarSystemLayer<T, V> renderLayer;

    public EinsteinSolarSystemRenderLayerParentImpl(EinsteinSolarSystemLayer<T, V> renderLayer) {
        this.renderLayer = renderLayer;
    }

    @Override
    public EinsteinSolarSystemModel<T> getModel() {
        return renderLayer.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(T player) {
        return renderLayer.getTextureLocation(player);
    }
}

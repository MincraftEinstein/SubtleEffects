package einstein.subtle_effects.client.renderer.entity;

import einstein.subtle_effects.client.model.entity.EinsteinSolarSystemModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;

public record EinsteinSolarSystemRenderLayerParentImpl<T extends AbstractClientPlayer, V extends PlayerModel<T>>(
        EinsteinSolarSystemLayer<T, V> renderLayer) implements RenderLayerParent<T, EinsteinSolarSystemModel<T>> {

    @Override
    public EinsteinSolarSystemModel<T> getModel() {
        return renderLayer.getModel();
    }

    @Override
    public ResourceLocation getTextureLocation(T player) {
        return renderLayer.getTextureLocation(player);
    }
}

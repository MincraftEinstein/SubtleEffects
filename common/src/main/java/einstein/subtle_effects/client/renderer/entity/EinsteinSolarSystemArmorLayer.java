package einstein.subtle_effects.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class EinsteinSolarSystemArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends HumanoidArmorLayer<S, M, A> {

    public EinsteinSolarSystemArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSetl, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer, modelSetl, equipmentRenderer);
    }

}

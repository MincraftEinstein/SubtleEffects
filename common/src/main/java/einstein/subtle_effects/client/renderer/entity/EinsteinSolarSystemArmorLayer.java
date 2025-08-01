package einstein.subtle_effects.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;

public class EinsteinSolarSystemArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends HumanoidArmorLayer<S, M, A> {

    public EinsteinSolarSystemArmorLayer(RenderLayerParent<S, M> renderer, A innerModel, A outerModel, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer, innerModel, outerModel, equipmentRenderer);
    }

    @Override
    protected void setPartVisibility(A model, EquipmentSlot slot) {
        model.setAllVisible(false);

        if (slot == EquipmentSlot.HEAD) {
            model.head.visible = true;
            model.hat.visible = true;
        }
    }
}

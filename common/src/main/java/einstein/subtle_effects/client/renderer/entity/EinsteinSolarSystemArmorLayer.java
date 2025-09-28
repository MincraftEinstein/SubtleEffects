package einstein.subtle_effects.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class EinsteinSolarSystemArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {

    public EinsteinSolarSystemArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager) {
        super(renderer, innerModel, outerModel, modelManager);
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

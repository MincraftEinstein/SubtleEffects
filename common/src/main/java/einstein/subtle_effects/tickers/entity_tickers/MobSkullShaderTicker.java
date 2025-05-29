package einstein.subtle_effects.tickers.entity_tickers;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MobSkullShaderTicker extends EntityTicker<Player> {

    private ItemStack oldHelmetStack = ItemStack.EMPTY;

    private CameraType oldCameraType;

    public MobSkullShaderTicker(Player entity) {
        super(entity);
    }

    @Override
    public void entityTick() {
        CameraType cameraType = Minecraft.getInstance().options.getCameraType();
        ItemStack helmetStack = entity.getItemBySlot(EquipmentSlot.HEAD);

        if ((oldHelmetStack.isEmpty() != helmetStack.isEmpty())
                || !ItemStack.isSameItem(oldHelmetStack, helmetStack)) {
            oldHelmetStack = helmetStack.copy();
            Util.applyHelmetShader(helmetStack, cameraType);
        }

        if (oldCameraType != cameraType) {
            oldCameraType = cameraType;

            Util.applyHelmetShader(helmetStack, cameraType);
        }
    }
}

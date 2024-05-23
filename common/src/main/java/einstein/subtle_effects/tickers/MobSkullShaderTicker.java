package einstein.subtle_effects.tickers;

import einstein.subtle_effects.util.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MobSkullShaderTicker extends Ticker<Player> {

    private ItemStack oldHelmetStack = ItemStack.EMPTY;

    private CameraType oldCameraType;

    public MobSkullShaderTicker(Player entity) {
        super(entity);
    }

    @Override
    public void tick() {
        ItemStack helmetStack = entity.getInventory().getArmor(3);
        if ((oldHelmetStack.isEmpty() != helmetStack.isEmpty())
                || !ItemStack.isSameItem(oldHelmetStack, helmetStack)) {
            oldHelmetStack = helmetStack.copy();
            Util.applyHelmetShader(helmetStack);
        }

        CameraType cameraType = Minecraft.getInstance().options.getCameraType();
        if (oldCameraType != cameraType) {
            oldCameraType = cameraType;

            if (cameraType.isFirstPerson()) {
                Util.applyHelmetShader(helmetStack);
            }
        }
    }
}

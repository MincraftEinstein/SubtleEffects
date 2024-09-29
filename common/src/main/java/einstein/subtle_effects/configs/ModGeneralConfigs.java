package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.ShaderManager;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ModGeneralConfigs extends Config {

    public boolean mobSkullShaders;

    @ValidatedInt.Restrict(min = 1, max = 32)
    public int particleRenderDistance = 5;

    public ModGeneralConfigs() {
        super(SubtleEffects.loc("general"));
    }

    @Override
    public void onUpdateClient() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.options.getCameraType().isFirstPerson()) {
            if (!mobSkullShaders) {
                ((ShaderManager) minecraft.gameRenderer).subtleEffects$clearShader();
                return;
            }

            Player player = minecraft.player;
            if (player != null) {
                Util.applyHelmetShader(player.getInventory().getArmor(3));
            }
        }
        TickerManager.clear();
    }
}

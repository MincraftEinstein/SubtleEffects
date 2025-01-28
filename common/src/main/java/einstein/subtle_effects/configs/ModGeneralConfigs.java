package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
import einstein.subtle_effects.util.ShaderManager;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

@Translation(prefix = ModConfigs.BASE_KEY + "general")
public class ModGeneralConfigs extends Config {

    public boolean mobSkullShaders;
    public ConfigGroup particleRenderingGroup = new ConfigGroup("particle_rendering");
    @ValidatedInt.Restrict(min = 1, max = 32)
    public int particleRenderDistance = 5;
    public boolean cullParticlesWithNoAlpha = true;
    @ConfigGroup.Pop
    public boolean cullParticlesInUnloadedChunks = true;
    public ValidatedFloat sparksScale = new ValidatedFloat(1, 2, 1);
    public ValidatedFloat fireHeight = new ValidatedFloat(0, 0.4F, -0.5F);
    public boolean poppingHearts = true;

    public ModGeneralConfigs() {
        super(SubtleEffects.loc("general"));
    }

    @Override
    public void onUpdateClient() {
        Minecraft minecraft = Minecraft.getInstance();
        TickerManager.clear();

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
    }
}

package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
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

    public ConfigGroup particleRenderingGroup = new ConfigGroup("particle_rendering");
    public boolean enableParticleCulling = true;
    @ValidatedInt.Restrict(min = 1, max = 32)
    public int particleRenderDistance = 5;
    public boolean cullParticlesWithNoAlpha = true;
    @ConfigGroup.Pop
    public boolean cullParticlesInUnloadedChunks = true;

    public ConfigGroup particlesGroup = new ConfigGroup("particles");
    public boolean lavaSparkSmoke = true;
    public ValidatedFloat sparksScale = new ValidatedFloat(1, 2, 1);
    public boolean poppingHearts = true;
    public boolean poppingBubbles = true;
    public ValidatedFloat poppingBubblesVolume = new ValidatedFloat(0.2F, 1, 0);
    public boolean glowingLavaDrops = true;
    public boolean fluidDropsEvaporate = true;
    public ValidatedFloat fluidDropsEvaporationVolume = new ValidatedFloat(0.25F, 1, 0);
    @ConfigGroup.Pop
    public boolean dropLandSounds = true;

    public boolean mobSkullShaders = true;
    public ValidatedFloat fireHeight = new ValidatedFloat(-0.15F, 0.4F, -0.5F);

    public ModGeneralConfigs() {
        super(SubtleEffects.loc("general"));
    }

    @Override
    public void onUpdateClient() {
        Minecraft minecraft = Minecraft.getInstance();
        TickerManager.clear();

        if (minecraft.level != null && minecraft.options.getCameraType().isFirstPerson()) {
            if (!mobSkullShaders) {
                minecraft.gameRenderer.shutdownEffect();
                return;
            }

            Player player = minecraft.player;
            if (player != null) {
                Util.applyHelmetShader(player.getInventory().getArmor(3));
            }
        }
    }
}

package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.ticking.tickers.TickerManager;
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
    public ValidatedInt particleRenderDistance = new ValidatedInt(5, 32, 1);
    public boolean cullParticlesInUnloadedChunks = true;
    public boolean allowUsingBlendedRenderType = true;
    @ConfigGroup.Pop
    public boolean spellParticlesUseBlendedRenderType = true;

    public ConfigGroup particlesGroup = new ConfigGroup("particles");

    public ConfigGroup dripParticlesGroup = new ConfigGroup("particles.drip_particles");
    public boolean glowingLavaDrops = true;
    public boolean fluidDropsEvaporate = true;
    public ValidatedFloat fluidDropsEvaporationVolume = new ValidatedFloat(0.25F, 1, 0);
    public boolean dropLandInFluidRipples = true;
    @ConfigGroup.Pop
    public ValidatedFloat dropLandSoundVolume = new ValidatedFloat(1, 2, 0);

    public boolean lavaSparkSmoke = true;
    public ValidatedFloat sparksScale = new ValidatedFloat(1, 2, 1);
    public boolean poppingHearts = true;
    public boolean poppingBubbles = true;
    public ValidatedFloat poppingBubblesVolume = new ValidatedFloat(0.2F, 1, 0);
    public ConfigGroup enchantmentParticlesGroup = new ConfigGroup("enchant_particles");
    public boolean glowingEnchantmentParticles = true;
    public boolean translucentEnchantmentParticles = true;
    @ConfigGroup.Pop
    public boolean disableRandomizedShading = true;
    public ValidatedFloat potionParticleAlpha = new ValidatedFloat(0.7F, 1, 0.3F);
    public ValidatedFloat potionParticleAlphaNearPlayer = new ValidatedFloat(0.2F, 1, 0);
    public ConfigGroup leavesGroup = new ConfigGroup("leaves");
    public boolean leavesLandOnGround = true;
    public boolean leavesLandOnWater = true;
    public boolean leavesLandingOnWaterRipples = true;
    public boolean leavesLandingOnWaterKeepMomentum = true;
    @ConfigGroup.Pop
    @ConfigGroup.Pop
    public ValidatedInt fallenLeavesLifeTime = new ValidatedInt(40, 100, 20);

    public boolean mobSkullShaders = true;
    public ValidatedFloat fireHeight = new ValidatedFloat(-0.15F, 0.4F, -0.5F);
    public boolean fireResistanceDisablesFireRendering = true;
    public boolean nightVisionFading = true;
    public ValidatedInt nightVisionFadingTime = new ValidatedInt(100, 200, 10);
    public boolean enableEasterEggs = true;

    public ModGeneralConfigs() {
        super(SubtleEffects.loc("general"));
    }

    @Override
    public void onUpdateClient() {
        Minecraft minecraft = Minecraft.getInstance();
        TickerManager.clear(Minecraft.getInstance().level);

        if (minecraft.level != null && minecraft.options.getCameraType().isFirstPerson()) {
            if (!mobSkullShaders) {
                minecraft.gameRenderer.shutdownEffect();
                return;
            }

            Player player = minecraft.player;
            if (player != null) {
                Util.applyHelmetShader(player.getInventory().getArmor(3), minecraft.options.getCameraType());
            }
        }
    }
}

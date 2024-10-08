package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.entities.*;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;

@Translation(prefix = ModConfigs.BASE_KEY + "entities")
public class ModEntityConfigs extends Config {

    public EntityAttackedConfigs attacked = new EntityAttackedConfigs();
    public SleepingConfigs sleeping = new SleepingConfigs();
    public DustCloudsConfig dustClouds = new DustCloudsConfig();
    public BurningEntityConfigs burning = new BurningEntityConfigs();
    public ItemRarityConfigs itemRarity = new ItemRarityConfigs();
    public PrimedTNTConfigs primedTNT = new PrimedTNTConfigs();

    public boolean enderPearlTrail = true;
    public final ValidatedDouble snowballTrailDensity = new ValidatedDouble(0.2, 1, 0);
    public final ValidatedDouble allayMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public final ValidatedDouble vexMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public boolean stomachGrowling = true;
    public boolean sheepShearFluff = true;
    public boolean improvedDragonFireballTrail = true;
    public final CommandBlockSpawnType commandBlockMinecartParticles = CommandBlockSpawnType.ON;
    public boolean heartBeating = true;
    public boolean endCrystalParticles = true;
    public boolean minecartLandingSparks = true;
    public boolean slimeTrails = true;
    public boolean magmaCubeTrails = true;
    public boolean replaceSlimeSquishParticles = true;
    public boolean replaceSpellCasterParticles = true;
    public boolean ironGolemCrackParticles = true;
    public boolean spectralArrowParticles = true;

    public ModEntityConfigs() {
        super(SubtleEffects.loc("entities"));
    }

    @Override
    public void onUpdateClient() {
        TickerManager.clear();
    }
}

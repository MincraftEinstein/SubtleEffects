package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.entities.*;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = ModConfigs.BASE_KEY + "entities")
public class ModEntityConfigs extends Config {

    public EntityAttackedConfigs attacked = new EntityAttackedConfigs();
    public SleepingConfigs sleeping = new SleepingConfigs();
    public DustCloudsConfig dustClouds = new DustCloudsConfig();
    public BurningEntityConfigs burning = new BurningEntityConfigs();
    public ItemRarityConfigs itemRarity = new ItemRarityConfigs();
    public PrimedTNTConfigs primedTNT = new PrimedTNTConfigs();

    public boolean enderPearlTrail = true;
    public ValidatedDouble snowballTrailDensity = new ValidatedDouble(0.2, 1, 0);
    public ValidatedDouble allayMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public ValidatedDouble vexMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public boolean sheepShearFluff = true;
    public boolean improvedDragonFireballTrail = true;
    public CommandBlockSpawnType commandBlockMinecartParticles = CommandBlockSpawnType.ON;
    public ValidatedInt stomachGrowlingThreshold = new ValidatedInt(6, 20, 0);
    public ValidatedFloat stomachGrowlingVolume = new ValidatedFloat(1, 1, 0);
    public ValidatedInt heartBeatingThreshold = new ValidatedInt(6, 20, 0);
    public ValidatedFloat heartbeatVolume = new ValidatedFloat(1, 1, 0);
    public boolean endCrystalParticles = true;
    public boolean minecartLandingSparks = true;
    public boolean slimeTrails = true;
    public boolean magmaCubeTrails = true;
    public boolean replaceSlimeSquishParticles = true;
    public boolean replaceSpellCasterParticles = true;
    public boolean ironGolemCrackParticles = true;
    public boolean spectralArrowParticles = true;
    public boolean wardenDeathSoulParticles = true;
    public PerspectiveType drowningBubbles = PerspectiveType.DEFAULT;
    public ValidatedInt drowningBubblesDensity = new ValidatedInt(10, 15, 3);
    public PerspectiveType frostyBreath = PerspectiveType.DEFAULT;
    public ValidatedFloat frostyBreathAlpha = new ValidatedFloat(0.5F, 1, 0.2F, ValidatedNumber.WidgetType.SLIDER);
    public ValidatedInt frostyBreathTime = new ValidatedInt(60, 200, 10);
    public FrostyBreathSeasons frostyBreathSeasons = FrostyBreathSeasons.DEFAULT;

    public ModEntityConfigs() {
        super(SubtleEffects.loc("entities"));
    }

    @Override
    public void onUpdateClient() {
        TickerManager.clear();
    }

    public enum PerspectiveType implements EnumTranslatable {
        OFF,
        DEFAULT,
        THIRD_PERSON_ONLY;

        public boolean isEnabled() {
            return this != OFF;
        }

        public boolean test(Minecraft minecraft) {
            return this == DEFAULT || (this == THIRD_PERSON_ONLY && !minecraft.options.getCameraType().isFirstPerson());
        }

        @NotNull
        @Override
        public String prefix() {
            return ModConfigs.BASE_KEY + "entities.perspectiveType";
        }
    }

    public enum FrostyBreathSeasons implements EnumTranslatable {
        OFF,
        DEFAULT,
        WINTER_ONLY;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "entities.frostyBreathSeasons";
        }
    }
}

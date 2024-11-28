package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.entities.*;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@Translation(prefix = ModConfigs.BASE_KEY + "entities")
public class ModEntityConfigs extends Config {

    public EntityAttackedConfigs attacked = new EntityAttackedConfigs();
    public SleepingConfigs sleeping = new SleepingConfigs();
    public HumanoidConfigs humanoids = new HumanoidConfigs();
    public DustCloudsConfig dustClouds = new DustCloudsConfig();
    public BurningEntityConfigs burning = new BurningEntityConfigs();
    public ItemRarityConfigs itemRarity = new ItemRarityConfigs();
    public ExplosivesConfigs explosives = new ExplosivesConfigs();

    public boolean enderPearlTrail = true;
    public ValidatedDouble snowballTrailDensity = new ValidatedDouble(0.2, 1, 0);
    public ValidatedDouble allayMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public ValidatedDouble vexMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public boolean sheepShearFluff = true;
    public boolean improvedDragonFireballTrail = true;
    public CommandBlockSpawnType commandBlockMinecartParticles = CommandBlockSpawnType.ON;
    public boolean endCrystalParticles = true;
    public boolean minecartLandingSparks = true;
    public boolean slimeTrails = true;
    public boolean magmaCubeTrails = true;
    public boolean replaceSlimeSquishParticles = true;
    public boolean replaceSpellCasterParticles = true;
    public boolean ironGolemCrackParticles = true;
    public boolean spectralArrowParticles = true;
    public boolean wardenDeathSoulParticles = true;
    public XPBottleParticlesDisplayType xpBottleParticlesDisplayType = XPBottleParticlesDisplayType.DEFAULT;
    public ValidatedInt xpBottleParticlesDensity = new ValidatedInt(10, 30, 5);

    public ModEntityConfigs() {
        super(SubtleEffects.loc("entities"));
    }

    @Override
    public void onUpdateClient() {
        TickerManager.clear();
    }

    public enum PerspectiveDisplayType implements EnumTranslatable {
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
            return ModConfigs.BASE_KEY + "entities.perspectiveDisplayType";
        }
    }

    public enum XPBottleParticlesDisplayType implements EnumTranslatable {
        DEFAULT,
        VANILLA,
        BOTH;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "entities.xpBottleParticlesDisplayType";
        }
    }
}

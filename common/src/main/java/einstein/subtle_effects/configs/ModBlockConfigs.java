package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.blocks.SparksConfigs;
import einstein.subtle_effects.configs.blocks.SteamConfigs;
import einstein.subtle_effects.configs.blocks.UpdatedSmokeConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.tickers.TickerManager;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks")
public class ModBlockConfigs extends Config {

    public SparksConfigs sparks = new SparksConfigs();
    public UpdatedSmokeConfigs updatedSmoke = new UpdatedSmokeConfigs();
    public SteamConfigs steam = new SteamConfigs();

    public boolean redstoneBlockDust = true;
    public BlockDustDensity redstoneBlockDustDensity = BlockDustDensity.DEFAULT;
    public GlowstoneDustDisplayType glowstoneBlockDustDisplayType = GlowstoneDustDisplayType.ON;
    public BlockDustDensity glowstoneBlockDustDensity = BlockDustDensity.DEFAULT;
    public boolean beehivesHaveSleepingZs = true;
    public boolean fallingBlockDust = true;
    public ValidatedList<ResourceLocation> fallingBlockDustBlocks = new ValidatedIdentifier().toList(getDefaultFallingBlockDustBlocks());
    public SmokeType torchflowerSmoke = SmokeType.DEFAULT;
    public boolean torchflowerFlames = true;
    public boolean dragonEggParticles = true;
    public boolean replaceEndPortalSmoke = true;
    public boolean pumpkinCarvedParticles = true;
    public boolean anvilBreakParticles = true;
    public boolean anvilUseParticles = true;
    public boolean grindstoneUseParticles = true;
    public CommandBlockSpawnType commandBlockParticles = CommandBlockSpawnType.ON;
    public boolean slimeBlockBounceSounds = true;
    public boolean beaconParticles = true;
    public boolean compostingParticles = true;
    public boolean respawnAnchorParticles = true;
    public boolean beehiveShearParticles = true;
    public boolean endPortalParticles = true;
    public boolean leavesDecayEffects = true;
    public boolean farmlandDestroyEffects = true;
    public AmethystSparkleDisplayType amethystSparkleDisplayType = AmethystSparkleDisplayType.ON;
    public boolean floweringAzaleaPetals = true;

    public ModBlockConfigs() {
        super(SubtleEffects.loc("blocks"));
    }

    @Override
    public void onUpdateClient() {
        TickerManager.clear();
    }

    private static List<ResourceLocation> getDefaultFallingBlockDustBlocks() {
        List<ResourceLocation> list = new ArrayList<>();
        list.add(new ResourceLocation("sand"));
        list.add(new ResourceLocation("suspicious_sand"));
        list.add(new ResourceLocation("red_sand"));
        list.add(new ResourceLocation("gravel"));
        list.add(new ResourceLocation("suspicious_gravel"));

        for (DyeColor color : DyeColor.values()) {
            list.add(new ResourceLocation(color.getName() + "_concrete_powder"));
        }
        return list;
    }

    public enum GlowstoneDustDisplayType implements EnumTranslatable {
        OFF,
        ON,
        NETHER_ONLY;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "blocks.glowstoneBlockDustDisplayType";
        }
    }

    public enum BlockDustDensity implements EnumTranslatable {
        DEFAULT(0),
        MINIMAL(2);

        private final int perSideChance;

        BlockDustDensity(int perSideChance) {
            this.perSideChance = perSideChance;
        }

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "blocks.blockDustDensity";
        }

        public int getPerSideChance() {
            return perSideChance;
        }
    }

    public enum AmethystSparkleDisplayType implements EnumTranslatable {
        OFF,
        ON,
        CRYSTALS_ONLY;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "blocks.amethystSparkleDisplayType";
        }
    }
}

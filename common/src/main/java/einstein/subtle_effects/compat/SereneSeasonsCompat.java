package einstein.subtle_effects.compat;

import einstein.subtle_effects.configs.entities.humanoids.FrostyBreathConfigs;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModConfig;

public class SereneSeasonsCompat {

    public static boolean isColdSeason(Level level, FrostyBreathConfigs.Seasons seasonsConfig) {
        if (seasonsConfig == FrostyBreathConfigs.Seasons.OFF) {
            return false;
        }

        if (!ModConfig.seasons.isDimensionWhitelisted(level.dimension())) {
            return false;
        }

        ISeasonState seasonState = SeasonHelper.getSeasonState(level);
        Season season = seasonState.getSeason();

        if (season == Season.WINTER) {
            return true;
        }

        if (seasonsConfig == FrostyBreathConfigs.Seasons.DEFAULT) {
            Season.SubSeason subSeason = seasonState.getSubSeason();
            return subSeason == Season.SubSeason.LATE_AUTUMN || subSeason == Season.SubSeason.EARLY_SPRING;
        }
        return false;
    }
}

package einstein.subtle_effects.compat;

import einstein.subtle_effects.configs.ModEntityConfigs;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SereneSeasonsCompat {

    public static boolean isColdSeason(Level level) {
        if (ENTITIES.frostyBreathSeasons == ModEntityConfigs.FrostyBreathSeasons.OFF) {
            return false;
        }

        ISeasonState seasonState = SeasonHelper.getSeasonState(level);
        Season season = seasonState.getSeason();

        if (season == Season.WINTER) {
            return true;
        }

        if (ENTITIES.frostyBreathSeasons == ModEntityConfigs.FrostyBreathSeasons.DEFAULT) {
            Season.SubSeason subSeason = seasonState.getSubSeason();
            return subSeason == Season.SubSeason.LATE_AUTUMN || subSeason == Season.SubSeason.EARLY_SPRING;
        }
        return false;
    }
}

package einstein.subtle_effects.compat;

import einstein.subtle_effects.configs.entities.humanoids.FrostyBreathConfigs;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class SereneSeasonsCompat {

    public static boolean isColdSeason(Level level) {
        if (ENTITIES.humanoids.frostyBreath.seasons == FrostyBreathConfigs.Seasons.OFF) {
            return false;
        }

        ISeasonState seasonState = SeasonHelper.getSeasonState(level);
        Season season = seasonState.getSeason();

        if (season == Season.WINTER) {
            return true;
        }

        if (ENTITIES.humanoids.frostyBreath.seasons == FrostyBreathConfigs.Seasons.DEFAULT) {
            Season.SubSeason subSeason = seasonState.getSubSeason();
            return subSeason == Season.SubSeason.LATE_AUTUMN || subSeason == Season.SubSeason.EARLY_SPRING;
        }
        return false;
    }
}

package einstein.subtle_effects.util;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.BiFunction;

public enum HolidayHelper {

    MOD_BIRTHDAY((month, dayOfMonth) -> month == Month.OCTOBER && dayOfMonth >= 3 && dayOfMonth <= 5);

    private final boolean function;

    HolidayHelper(BiFunction<Month, Integer, Boolean> function) {
        LocalDate date = LocalDate.now();
        this.function = function.apply(date.getMonth(), date.getDayOfMonth());
    }

    public boolean get() {
        return function;
    }

    public boolean getEnabledInDev() {
        return function || ConfigApiJava.platform().isDev();
    }
}

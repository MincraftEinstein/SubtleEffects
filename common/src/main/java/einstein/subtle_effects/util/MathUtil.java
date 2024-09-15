package einstein.subtle_effects.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MathUtil {

    public static double nextNonAbsDouble(RandomSource random) {
        return random.nextDouble() * nextSign(random);
    }

    public static double nextNonAbsDouble(RandomSource random, double max) {
        return nextDouble(random, max) * nextSign(random);
    }

    public static double nextNonAbsDouble(RandomSource random, double min, double max) {
        return Mth.nextDouble(random, min, max) * nextSign(random);
    }

    public static double nextDouble(RandomSource random, double max) {
        return Mth.nextDouble(random, 0, max);
    }

    public static int nextSign(RandomSource random) {
        return random.nextBoolean() ? 1 : -1;
    }
}

package einstein.ambient_sleep.util;

import net.minecraft.util.RandomSource;

public class MathUtil {

    private static final RandomSource RANDOM = RandomSource.create();

    public static double nextNonAbsDouble() {
        return RANDOM.nextDouble() * nextSign();
    }

    public static int nextSign() {
        return RANDOM.nextBoolean() ? 1 : -1;
    }

    public static float nextFloat(int max) {
        return RANDOM.nextInt(max) / 100F;
    }

    public static float nextFloat(int min, int max) {
        return RANDOM.nextInt(min, max) / 100F;
    }
}

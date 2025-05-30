package einstein.subtle_effects.util;

import net.minecraft.util.Mth;

// Backported from 1.21
public record LifetimeAlpha(float startAlpha, float endAlpha, float startAtNormalizedAge, float endAtNormalizedAge) {

    public static final LifetimeAlpha ALWAYS_OPAQUE = new LifetimeAlpha(1, 1, 0, 1);

    public boolean isOpaque() {
        return startAlpha >= 1 && endAlpha >= 1;
    }

    public float currentAlphaForAge(int age, int lifetime, float partialTick) {
        if (Mth.equal(startAlpha, endAlpha)) {
            return startAlpha;
        }
        return Mth.clampedLerp(startAlpha, endAlpha, Mth.inverseLerp((age + partialTick) / lifetime, startAtNormalizedAge, endAtNormalizedAge));
    }
}
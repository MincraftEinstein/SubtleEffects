package einstein.subtle_effects.configs.cache;

/**
 * Snapshot caches for config values read on particle/tick hot paths.
 * <p>
 * Fzzy Config's {@code ValidatedField.get()} is fine for UI and infrequent logic,
 * but particle render and animate-tick paths call it thousands of times per second.
 * These caches hold the resolved values and are refreshed when configs load or change.
 */
public final class HotPathConfigCaches {

    private HotPathConfigCaches() {
    }

    public static void refreshAll() {
        ParticleCullingCache.refresh();
        FireflyConfigCache.refresh();
    }
}

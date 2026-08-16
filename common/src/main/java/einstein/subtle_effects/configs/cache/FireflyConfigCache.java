package einstein.subtle_effects.configs.cache;

import einstein.subtle_effects.configs.ColdSeasonsType;
import einstein.subtle_effects.configs.environment.FireflyConfigs;
import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Set;

/**
 * Cached firefly settings used from animate-tick (many block positions per tick).
 * Membership lists are sets; spawn/sound odds are precomputed so the hot path avoids
 * both config lookups and repeated arithmetic.
 */
public final class FireflyConfigCache {

    public static boolean enabled;
    public static Set<ResourceLocation> dimensionBlocklist = Set.of();
    public static Set<ResourceLocation> biomesBlocklist = Set.of();
    public static Set<ResourceLocation> biomesAllowlist = Set.of();
    public static Set<Block> spawnableBlocks = Set.of();
    public static ColdSeasonsType ignoredSeasons = ColdSeasonsType.DEFAULT;
    public static FireflyConfigs.FireflyType fireflyType = FireflyConfigs.FireflyType.ORIGINAL;
    public static SimpleParticleType particle;
    public static float soundVolume;
    public static boolean soundsEnabled;
    public static boolean onlyAllowInHabitatBiomes;
    public static Set<ResourceLocation> habitatBiomes = Set.of();
    public static double habitatSpawnChance;
    public static double defaultSpawnChance;
    public static double habitatSoundChance;
    public static double defaultSoundChance;

    private FireflyConfigCache() {
    }

    public static void refresh() {
        var fireflies = ModConfigs.ENVIRONMENT.fireflies;

        enabled = fireflies.firefliesEnabled.get();
        dimensionBlocklist = Set.copyOf(fireflies.dimensionBlocklist.get());
        biomesBlocklist = Set.copyOf(fireflies.biomesBlocklist.get());
        biomesAllowlist = Set.copyOf(fireflies.biomesAllowlist.get());
        spawnableBlocks = Set.copyOf(fireflies.spawnableBlocks.get());
        ignoredSeasons = fireflies.ignoredSeasons.get();
        fireflyType = fireflies.fireflyType.get();
        particle = fireflyType.getParticle().get();
        soundVolume = fireflies.fireflySoundVolume.get();
        soundsEnabled = soundVolume > 0;
        onlyAllowInHabitatBiomes = fireflies.onlyAllowInHabitatBiomes.get();
        habitatBiomes = Set.copyOf(fireflies.habitatBiomes.get());

        int habitatDensity = fireflies.habitatBiomeDensity.get();
        int defaultDensity = fireflies.defaultDensity.get();
        int spawnRate = fireflyType == FireflyConfigs.FireflyType.VANILLA ? 170 : 1;

        habitatSpawnChance = 0.0005 * habitatDensity;
        defaultSpawnChance = 0.008 * defaultDensity * spawnRate;
        habitatSoundChance = 0.00001 * habitatDensity;
        defaultSoundChance = 0.0003 * defaultDensity;
    }
}

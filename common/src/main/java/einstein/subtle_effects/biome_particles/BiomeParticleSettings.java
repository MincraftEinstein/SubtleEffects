package einstein.subtle_effects.biome_particles;

import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class BiomeParticleSettings {

    private final ValidatedList<ResourceLocation> biomesConfig;
    private final int density;
    private final int maxSpawnHeight;
    private final Supplier<? extends ParticleOptions> particle;
    private final BiPredicate<Level, BlockPos> spawnConditions;
    private final boolean ignoreHeight;
    private List<Biome> biomes;

    public BiomeParticleSettings(ValidatedList<ResourceLocation> biomesConfig,
                                 int density, int maxSpawnHeight,
                                 Supplier<? extends ParticleOptions> particle,
                                 BiPredicate<Level, BlockPos> spawnConditions, boolean ignoreHeight) {
        this.biomesConfig = biomesConfig;
        this.density = density;
        this.maxSpawnHeight = maxSpawnHeight;
        this.particle = particle;
        this.spawnConditions = spawnConditions;
        this.ignoreHeight = ignoreHeight;
    }

    public List<Biome> getBiomes(Level level) {
        if (biomes == null) {
            biomes = biomesConfig.stream().map(location ->
                    level.registryAccess().registryOrThrow(Registries.BIOME).get(location)
            ).filter(Objects::nonNull).toList();
        }
        return biomes;
    }

    public void clear() {
        biomes = null;
    }

    public int getDensity() {
        return density;
    }

    public int getMaxSpawnHeight() {
        return maxSpawnHeight;
    }

    public Supplier<? extends ParticleOptions> getParticle() {
        return particle;
    }

    public boolean checkSpawnConditions(Level level, BlockPos pos) {
        return spawnConditions.test(level, pos);
    }

    public boolean ignoreHeight() {
        return ignoreHeight;
    }
}

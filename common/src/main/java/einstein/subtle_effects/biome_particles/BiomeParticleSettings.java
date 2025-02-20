package einstein.subtle_effects.biome_particles;

import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class BiomeParticleSettings {

    private final ValidatedList<ResourceLocation> biomesConfig;
    private final ValidatedInt density;
    private final int maxSpawnHeight;
    private final Supplier<? extends ParticleOptions> particle;
    private final BiPredicate<Level, BlockPos> spawnConditions;
    private final boolean ignoreHeight;
    private final List<Biome> biomes = new ArrayList<>();

    public BiomeParticleSettings(ValidatedList<ResourceLocation> biomesConfig,
                                 ValidatedInt density, int maxSpawnHeight,
                                 Supplier<? extends ParticleOptions> particle,
                                 BiPredicate<Level, BlockPos> spawnConditions, boolean ignoreHeight) {
        this.biomesConfig = biomesConfig;
        this.density = density;
        this.maxSpawnHeight = maxSpawnHeight;
        this.particle = particle;
        this.spawnConditions = spawnConditions;
        this.ignoreHeight = ignoreHeight;
    }

    public void update(Level level) {
        biomes.addAll(biomesConfig.stream().map(location ->
                level.registryAccess().lookupOrThrow(Registries.BIOME).get(location)
        ).filter(Optional::isPresent).map(biomeReference -> biomeReference.get().value()).toList());
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

    public void clear() {
        biomes.clear();
    }

    public int getDensity() {
        return density.get();
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

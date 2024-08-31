package einstein.subtle_effects.biome_particles;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BiomeParticleSettings {

    private final ModConfigSpec.ConfigValue<List<? extends String>> biomesConfig;
    private final ModConfigSpec.IntValue density;
    private final int maxSpawnHeight;
    private final Supplier<? extends ParticleOptions> particle;
    private final Predicate<Level> spawnConditions;
    private final boolean ignoreHeight;
    private List<Biome> biomes;

    public BiomeParticleSettings(ModConfigSpec.ConfigValue<List<? extends String>> biomesConfig,
                                 ModConfigSpec.IntValue density, int maxSpawnHeight,
                                 Supplier<? extends ParticleOptions> particle,
                                 Predicate<Level> spawnConditions, boolean ignoreHeight) {
        this.biomesConfig = biomesConfig;
        this.density = density;
        this.maxSpawnHeight = maxSpawnHeight;
        this.particle = particle;
        this.spawnConditions = spawnConditions;
        this.ignoreHeight = ignoreHeight;
    }

    public List<Biome> getBiomes(Level level) {
        if (biomes == null) {
            biomes = biomesConfig.get().stream().map(string -> {
                ResourceLocation location = ResourceLocation.tryParse(string);
                if (location != null) {
                    Registry<Biome> registry = level.registryAccess().registryOrThrow(Registries.BIOME);
                    return registry.get(location);
                }
                return null;
            }).filter(Objects::nonNull).toList();
        }
        return biomes;
    }

    public void clear() {
        biomes = null;
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

    public boolean checkSpawnConditions(Level level) {
        return spawnConditions.test(level);
    }

    public boolean ignoreHeight() {
        return ignoreHeight;
    }
}

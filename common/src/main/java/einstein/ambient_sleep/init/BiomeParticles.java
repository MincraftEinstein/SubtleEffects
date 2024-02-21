package einstein.ambient_sleep.init;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class BiomeParticles {

    public static final List<BiomeParticleSettings> BIOME_PARTICLE_SETTINGS = new ArrayList<>();

    public static final BiomeParticleSettings MUSHROOM_FIELDS = register(ModConfigs.INSTANCE.mushroomSporeBiomes, 8, 40, ModParticles.MUSHROOM_SPORE.get(), level -> true);
    public static final BiomeParticleSettings FIREFLIES = register(ModConfigs.INSTANCE.fireflyBiomes, 6, 20, ModParticles.FIREFLY.get(), level -> level.getDayTime() > 13000 && level.getDayTime() < 23000);

    public static void init() {
    }

    private static BiomeParticleSettings register(ForgeConfigSpec.ConfigValue<List<? extends String>> biomesConfig, int spawnChance, int maxSpawnHeight, ParticleOptions particle, Predicate<Level> spawnConditions) {
        BiomeParticleSettings settings = new BiomeParticleSettings(biomesConfig, spawnChance, maxSpawnHeight, particle, spawnConditions);
        BIOME_PARTICLE_SETTINGS.add(settings);
        return settings;
    }

    public record BiomeParticleSettings(ForgeConfigSpec.ConfigValue<List<? extends String>> biomesConfig,
                                        int spawnChance, int maxSpawnHeight, ParticleOptions particle,
                                        Predicate<Level> spawnConditions) {

        public List<Biome> getBiomes(Level level) {
            return biomesConfig.get().stream().map(s -> {
                ResourceLocation location = ResourceLocation.tryParse(s);
                if (location != null) {
                    Registry<Biome> registry = level.registryAccess().registryOrThrow(Registries.BIOME);
                    return registry.get(location);
                }
                return null;
            }).filter(Objects::nonNull).toList();
        }
    }
}

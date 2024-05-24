package einstein.subtle_effects.biome_particles;

import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.INSTANCE;

public class BiomeParticleManager {

    private static final List<BiomeParticleSettings> REGISTERED = new ArrayList<>();
    private static final BlockPos.MutableBlockPos BIOME_POS = new BlockPos.MutableBlockPos();
    private static final Predicate<Level> NOT_RAINING = level -> !level.isRaining();

    public static void init() {
        register(INSTANCE.mushroomSporeBiomes, INSTANCE.mushroomSporeDensity, 40, ModParticles.MUSHROOM_SPORE, NOT_RAINING);
        register(INSTANCE.fireflyBiomes, INSTANCE.fireflyDensity, 20, ModParticles.FIREFLY, level -> NOT_RAINING.test(level) && level.getDayTime() > 13000 && level.getDayTime() < 23000);
        register(INSTANCE.pollenBiomes, INSTANCE.pollenDensity, 10, ModParticles.POLLEN, NOT_RAINING);
        register(INSTANCE.sculkDustBiomes, INSTANCE.sculkDustDensity, ModParticles.SCULK_DUST, level -> true);
    }

    private static void register(ForgeConfigSpec.ConfigValue<List<? extends String>> biomesConfig, ForgeConfigSpec.IntValue density, int maxSpawnHeight, Supplier<? extends ParticleOptions> particle, Predicate<Level> spawnConditions) {
        REGISTERED.add(new BiomeParticleSettings(biomesConfig, density, maxSpawnHeight, particle, spawnConditions, false));
    }

    private static void register(ForgeConfigSpec.ConfigValue<List<? extends String>> biomesConfig, ForgeConfigSpec.IntValue density, Supplier<? extends ParticleOptions> particle, Predicate<Level> spawnConditions) {
        REGISTERED.add(new BiomeParticleSettings(biomesConfig, density, 0, particle, spawnConditions, true));
    }

    public static void tickBiomeParticles(Level level, Player player) {
        int radius = INSTANCE.biomeParticlesRadius.get();

        if (radius <= 0) {
            return;
        }

        for (int i = 0; i < 100; i++) {
            RandomSource random = level.getRandom();
            int x = player.getBlockX() + random.nextInt(radius) - random.nextInt(radius);
            int y = player.getBlockY() + random.nextInt(radius) - random.nextInt(radius);
            int z = player.getBlockZ() + random.nextInt(radius) - random.nextInt(radius);
            BIOME_POS.set(x, y, z);

            if (level.isOutsideBuildHeight(y)) {
                continue;
            }

            Holder<Biome> biome = level.getBiome(BIOME_POS);
            for (BiomeParticleSettings settings : REGISTERED) {
                if (settings.getDensity() > i && settings.checkSpawnConditions(level)) {
                    List<Biome> biomes = settings.getBiomes(level);
                    if (biomes.isEmpty()) {
                        continue;
                    }

                    if (!settings.ignoreHeight()) {
                        int surfaceLevel = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                        if (surfaceLevel > y || (surfaceLevel + settings.getMaxSpawnHeight()) < y) {
                            continue;
                        }
                    }

                    if (biomes.contains(biome.value())) {
                        BlockState state = level.getBlockState(BIOME_POS);
                        if (!state.isCollisionShapeFullBlock(level, BIOME_POS)) {
                            level.addParticle(settings.getParticle().get(), x + random.nextDouble(), y + random.nextDouble(), z + random.nextDouble(), 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    public static void clear() {
        REGISTERED.forEach(BiomeParticleSettings::clear);
    }
}

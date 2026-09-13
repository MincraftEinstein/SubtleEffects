package einstein.subtle_effects.ticking;

import einstein.subtle_effects.init.ModParticles;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;

/**
 * Ambient biome particles driven from animate-tick.
 * Config values are snapshotted in {@link #init()} so the tick path does not call {@code ValidatedField.get()}.
 */
public class BiomeEffectsManager {

    private static final List<Options> REGISTERED = new ArrayList<>();
    private static final BiPredicate<Level, BlockPos> ALWAYS = (level, pos) -> true;

    public static void init() {
        REGISTERED.clear();
        register(ModParticles.MUSHROOM_SPORE, 40, ENVIRONMENT.biomes.mushroomSporeDensity, ENVIRONMENT.biomes.mushroomSporeBiomes);
        register(ModParticles.POLLEN, 10, ENVIRONMENT.biomes.pollenDensity, ENVIRONMENT.biomes.pollenBiomes);
        register(ModParticles.SCULK_DUST, 15, ENVIRONMENT.biomes.sculkDustDensity, ENVIRONMENT.biomes.sculkDustBiomes);
    }

    private static void register(Supplier<? extends ParticleOptions> particleOptions, int maxHeight, ValidatedField<Float> chance, ValidatedField<List<? extends ResourceLocation>> biomesConfig) {
        float resolvedChance = chance.get();
        List<? extends ResourceLocation> biomes = biomesConfig.get();
        if (!biomes.isEmpty() && resolvedChance > 0) {
            REGISTERED.add(new Options(particleOptions.get(), maxHeight, ALWAYS, resolvedChance, Set.copyOf(biomes)));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RandomSource random) {
        if (REGISTERED.isEmpty()) {
            return;
        }

        if (!state.isCollisionShapeFullBlock(level, pos) && level.getFluidState(pos).isEmpty()) {
            Optional<ResourceLocation> biomeId = level.getBiome(pos).unwrapKey().map(ResourceKey::location);
            if (biomeId.isEmpty()) {
                return;
            }

            ResourceLocation id = biomeId.get();
            boolean isRaining = level.isRaining();
            BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
            int surfaceLevel = surfacePos.getY();
            int y = pos.getY();

            for (Options options : REGISTERED) {
                if (!options.biomes().contains(id)) {
                    continue;
                }

                if (random.nextDouble() * 100 >= options.chance()) {
                    continue;
                }

                if (y < surfaceLevel) {
                    Optional<ResourceLocation> surfaceBiomeId = level.getBiome(surfacePos).unwrapKey().map(ResourceKey::location);
                    if (surfaceBiomeId.isPresent() && surfaceBiomeId.get().equals(id) && (level.getBrightness(LightLayer.SKY, pos) < 7 || isRaining)) {
                        return;
                    }
                }
                else if (isRaining || y > surfaceLevel + options.maxHeight()) {
                    return;
                }

                if (options.conditions().test(level, pos)) {
                    level.addParticle(options.particleOptions(),
                            pos.getX() + random.nextDouble(),
                            y + random.nextDouble(),
                            pos.getZ() + random.nextDouble(),
                            0, 0, 0
                    );
                }
            }
        }
    }

    private record Options(ParticleOptions particleOptions, int maxHeight,
                           BiPredicate<Level, BlockPos> conditions, float chance,
                           Set<ResourceLocation> biomes) {
    }
}

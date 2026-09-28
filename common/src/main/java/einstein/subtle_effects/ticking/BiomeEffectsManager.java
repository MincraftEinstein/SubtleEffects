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
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;

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
        if (!biomesConfig.get().isEmpty() && chance.get() > 0) {
            REGISTERED.add(new Options(particleOptions, maxHeight, ALWAYS, chance, biomesConfig));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RandomSource random) {
        if (!state.isCollisionShapeFullBlock(level, pos) && level.getFluidState(pos).isEmpty()) {
            for (Options options : REGISTERED) {
                Optional<ResourceLocation> biomeId = level.getBiome(pos).unwrapKey().map(ResourceKey::location);
                if (biomeId.isPresent() && options.biomesConfig().get().contains(biomeId.get())) {
                    if (random.nextDouble() * 100 < options.chance().get()) {
                        boolean isRaining = level.isRaining();
                        BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
                        int surfaceLevel = surfacePos.getY();
                        int y = pos.getY();

                        if (y < surfaceLevel) {
                            Optional<ResourceLocation> surfaceBiomeId = level.getBiome(surfacePos).unwrapKey().map(ResourceKey::location);
                            if (surfaceBiomeId.isPresent() && surfaceBiomeId.get().equals(biomeId.get()) && (level.getBrightness(LightLayer.SKY, pos) < 7 || isRaining)) {
                                return;
                            }
                        }
                        else if (isRaining || y > surfaceLevel + options.maxHeight) {
                            return;
                        }

                        if (options.conditions().test(level, pos)) {
                            level.addParticle(options.particleOptions().get(),
                                    pos.getX() + random.nextDouble(),
                                    y + random.nextDouble(),
                                    pos.getZ() + random.nextDouble(),
                                    0, 0, 0
                            );
                        }
                    }
                }
            }
        }
    }

    private record Options(Supplier<? extends ParticleOptions> particleOptions, int maxHeight,
                           BiPredicate<Level, BlockPos> conditions, ValidatedField<Float> chance,
                           ValidatedField<List<? extends ResourceLocation>> biomesConfig) {

    }
}

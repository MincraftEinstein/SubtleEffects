package einstein.subtle_effects.biome_particles;

import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;

public class FireflyManager {

    public static void tick(Level level, BlockPos pos, BlockState state, RandomSource random) {
        if (!ENVIRONMENT.fireflies.firefliesEnabled) {
            return;
        }

        Optional<ResourceKey<DimensionType>> dimensionKey = level.dimensionTypeRegistration().unwrapKey();
        if (dimensionKey.isEmpty() || ENVIRONMENT.fireflies.dimensionBlocklist.contains(dimensionKey.get().location())) {
            return;
        }

        Holder<Biome> biome = level.getBiome(pos);
        Optional<ResourceKey<Biome>> biomeKey = biome.unwrapKey();
        if (biomeKey.isEmpty()) {
            return;
        }

        ResourceLocation biomeId = biomeKey.get().location();
        if (!ENVIRONMENT.fireflies.biomesBlocklist.contains(biomeId)) {
            boolean isHabitatBiome = ENVIRONMENT.fireflies.habitatBiomes.contains(biomeId);
            if (!isHabitatBiome && (ENVIRONMENT.fireflies.allowOnlyInHabitatBiomes || !ENVIRONMENT.fireflies.spawnableBlocks.contains(state.getBlock()))) {
                return;
            }

            int surfaceLevel = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY();
            boolean canSeeSky = level.canSeeSky(pos);
            if (isHabitatBiome && canSeeSky && surfaceLevel + 10 < pos.getY()) {
                return;
            }

            if (biome.value().warmEnoughToRain(pos) || ENVIRONMENT.fireflies.biomesAllowlist.contains(biomeId)) {
                float time = level.getDayTime() % 24000F;

                if (((time > 13000 && time < 23000) || level.getBrightness(LightLayer.SKY, pos) == 0) && level.getBrightness(LightLayer.BLOCK, pos) <= 5) {
                    if (!level.isRaining() || !canSeeSky || surfaceLevel > pos.getY()) {
                        if (!state.isCollisionShapeFullBlock(level, pos) && level.getFluidState(pos).isEmpty()) {

                            if (canSpawn(random, isHabitatBiome)) {
                                level.addParticle(ModParticles.FIREFLY.get(),
                                        pos.getX() + 0.5 + nextNonAbsDouble(random, 0.4375),
                                        pos.getY() + 0.5 + nextNonAbsDouble(random, 0.4375),
                                        pos.getZ() + 0.5 + nextNonAbsDouble(random, 0.4375),
                                        0, 0, 0
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean canSpawn(RandomSource random, boolean isHabitatBiome) {
        if (isHabitatBiome) {
            return random.nextDouble() < (0.0005 * ENVIRONMENT.fireflies.habitatBiomeDensity.get());
        }
        return random.nextDouble() < (0.008 * ENVIRONMENT.fireflies.defaultDensity.get());
    }
}

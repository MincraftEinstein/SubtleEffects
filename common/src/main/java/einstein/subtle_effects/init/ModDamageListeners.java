package einstein.subtle_effects.init;

import einstein.subtle_effects.util.EntityProvider;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.MathUtil.*;

public class ModDamageListeners {

    public static final Map<EntityType<?>, EntityProvider<?>> REGISTERED = new HashMap<>();

    public static void init() {
        register(EntityType.CHICKEN, (entity, level, random) -> {
            if (ENTITIES.damageTaken.damagedChickenFeathers) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ModParticles.CHICKEN_FEATHER.get(),
                            entity.getX(),
                            entity.getY(0.5),
                            entity.getZ(),
                            nextNonAbsDouble(random),
                            nextNonAbsDouble(random),
                            nextNonAbsDouble(random)
                    );
                }
            }
        });
        register(EntityType.PARROT, (entity, level, random) -> {
            if (ENTITIES.damageTaken.damagedParrotFeathers) {
                ParticleOptions particle = switch (entity.getVariant()) {
                    case BLUE -> ModParticles.BLUE_PARROT_FEATHER.get();
                    case GRAY -> ModParticles.GRAY_PARROT_FEATHER.get();
                    case GREEN -> ModParticles.GREEN_PARROT_FEATHER.get();
                    case RED_BLUE -> ModParticles.RED_BLUE_PARROT_FEATHER.get();
                    case YELLOW_BLUE -> ModParticles.YELLOW_BLUE_PARROT_FEATHER.get();
                };

                for (int i = 0; i < 5; i++) {
                    level.addParticle(particle,
                            entity.getX(),
                            entity.getY(0.5),
                            entity.getZ(),
                            nextNonAbsDouble(random),
                            nextNonAbsDouble(random),
                            nextNonAbsDouble(random)
                    );
                }
            }
        });
        register(EntityType.SNOW_GOLEM, (entity, level, random) -> {
            if (ENTITIES.damageTaken.damagedSnowGolemSnowflakes) {
                for (int i = 0; i < 20; i++) {
                    level.addParticle(ModParticles.SNOW.get(),
                            entity.getX(),
                            entity.getY(random.nextFloat()),
                            entity.getZ(),
                            nextNonAbsDouble(random),
                            nextNonAbsDouble(random),
                            nextNonAbsDouble(random)
                    );
                }
            }
        });
        register(EntityType.SHEEP, (entity, level, random) -> {
            if (ENTITIES.damageTaken.damagedSheepFluff) {
                ParticleSpawnUtil.spawnSheepFluff(entity, random.nextInt(3));
            }
        });
        register(EntityType.SLIME, (entity, level, random) -> {
            if (ENTITIES.damageTaken.damagedSlimeSlime) {
                boolean replaceSlimeSquishParticles = ENTITIES.replaceSlimeSquishParticles;
                ParticleOptions options = replaceSlimeSquishParticles
                        ? new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState())
                        : new ItemParticleOption(ParticleTypes.ITEM, Items.SLIME_BALL.getDefaultInstance());
                double maxSpeed = replaceSlimeSquishParticles ? 0.5 : 0.15;

                for (int i = 0; i < 10 * entity.getSize(); i++) {
                    int xSign = nextSign(random);
                    int zSign = nextSign(random);

                    level.addParticle(options,
                            entity.getX(nextDouble(random, 0.5) * xSign),
                            entity.getRandomY(),
                            entity.getZ(nextDouble(random, 0.5) * zSign),
                            nextDouble(random, maxSpeed) * xSign,
                            0,
                            nextDouble(random, maxSpeed) * zSign
                    );
                }
            }
        });
        register(EntityType.SKELETON, (entity, level, random) ->
                spawnBones(entity, level, random, ModParticles.SKELETON_BONE.get()));
        register(EntityType.WITHER_SKELETON, (entity, level, random) ->
                spawnBones(entity, level, random, ModParticles.WITHER_BONE.get()));
        register(EntityType.STRAY, (entity, level, random) ->
                spawnBones(entity, level, random, ModParticles.STRAY_BONE.get()));
        register(EntityType.BOGGED, (entity, level, random) ->
                spawnBones(entity, level, random, ModParticles.BOGGED_BONE.get()));
        register(EntityType.SKELETON_HORSE, (entity, level, random) -> {
            if (ENTITIES.damageTaken.damagedSkeletonHorseBones) {
                for (int i = 0; i < 7; i++) {
                    level.addParticle(ModParticles.SKELETON_BONE.get(),
                            entity.getRandomX(0.3),
                            entity.getY(0.5 + nextDouble(random, 0.5)),
                            entity.getRandomZ(0.3),
                            nextNonAbsDouble(random, 0.7),
                            nextNonAbsDouble(random) * 2,
                            nextNonAbsDouble(random, 0.7)
                    );
                }
            }
        });
    }

    private static void spawnBones(Entity entity, Level level, RandomSource random, ParticleOptions particle) {
        if (ENTITIES.damageTaken.damagedSkeletonBones) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(particle,
                        entity.getX(),
                        entity.getY(0.5 + nextDouble(random, 0.5)),
                        entity.getZ(),
                        nextNonAbsDouble(random, 0.7),
                        nextNonAbsDouble(random) * 2,
                        nextNonAbsDouble(random, 0.7)
                );
            }
        }
    }

    private static <T extends Entity> void register(EntityType<T> type, EntityProvider<T> provider) {
        REGISTERED.put(type, provider);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void spawnParticles(T entity, Level level, RandomSource random) {
        EntityType<T> type = (EntityType<T>) entity.getType();
        if (REGISTERED.containsKey(type)) {
            ((EntityProvider<T>) REGISTERED.get(type)).apply(entity, level, random);
        }
    }
}

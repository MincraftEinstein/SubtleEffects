package einstein.ambient_sleep.util;

import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.nextNonAbsDouble;
import static einstein.ambient_sleep.util.MathUtil.nextSign;

public class ParticleManager {

    private static final Map<EntityType<?>, EntityProvider<?>> ON_ENTITY_HURT_PROVIDERS = new HashMap<>();

    public static void init() {
        // Entity Hurt
        registerOnEntityHurtProvider(EntityType.CHICKEN, (entity, level, random) -> {
            if (INSTANCE.chickenHitFeathers.get()) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ModParticles.CHICKEN_FEATHER.get(),
                            entity.getX(),
                            entity.getY(0.5),
                            entity.getZ(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble()
                    );
                }
            }
        });
        registerOnEntityHurtProvider(EntityType.PARROT, (entity, level, random) -> {
            if (INSTANCE.parrotHitFeathers.get()) {
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
                            nextNonAbsDouble(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble()
                    );
                }
            }
        });
        registerOnEntityHurtProvider(EntityType.SNOW_GOLEM, (entity, level, random) -> {
            if (INSTANCE.snowGolemHitSnowflakes.get()) {
                for (int i = 0; i < 20; i++) {
                    level.addParticle(ModParticles.SNOW.get(),
                            entity.getX(),
                            entity.getY(random.nextFloat()),
                            entity.getZ(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble(),
                            nextNonAbsDouble()
                    );
                }
            }
        });
    }

    private static <T extends Entity> void registerOnEntityHurtProvider(EntityType<T> type, EntityProvider<T> provider) {
        ON_ENTITY_HURT_PROVIDERS.put(type, provider);
    }

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity> void entityHurt(T entity, Level level, RandomSource random) {
        EntityType<T> type = (EntityType<T>) entity.getType();
        if (ON_ENTITY_HURT_PROVIDERS.containsKey(type)) {
            ((EntityProvider<T>) ON_ENTITY_HURT_PROVIDERS.get(type)).apply(entity, level, random);
        }
    }

    public static void entityFell(LivingEntity entity, double y, float distance, int fallDamage) {
        if (entity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            return;
        }

        if (!INSTANCE.fallDamageDustClouds.get()) {
            return;
        }

        if (fallDamage <= 0 && !((entity instanceof AbstractHorse) && distance > (entity instanceof Camel ? 0.5 : 1))) {
            return;
        }

        if (entity.isInWater() || entity.isInLava() || entity.isInPowderSnow) {
            return;
        }

        Level level = entity.level();
        RandomSource random = entity.getRandom();

        if (entity instanceof Strider strider) {
            if (level.getFluidState(strider.getOnPos().atY(Mth.floor(y))).is(FluidTags.LAVA)) {
                return;
            }
        }

        if (fallDamage < 4) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ModParticles.SMALL_DUST_CLOUD.get(),
                        entity.getRandomX(1),
                        y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                        entity.getRandomZ(1),
                        0.3 * nextSign(),
                        random.nextDouble(),
                        0.3 * nextSign()
                );
            }
            return;
        }

        for (int i = 0; i < 10; i++) {
            level.addParticle(ModParticles.LARGE_DUST_CLOUD.get(),
                    entity.getRandomX(1),
                    y + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                    entity.getRandomZ(1),
                    0.5 * nextSign(),
                    random.nextDouble() * 3,
                    0.5 * nextSign()
            );
        }
    }

    @FunctionalInterface
    public interface EntityProvider<T extends Entity> {

        void apply(T entity, Level level, RandomSource random);
    }

    @FunctionalInterface
    public interface BlockProvider {

        void apply(BlockState state, Level level, BlockPos pos, RandomSource random);
    }
}

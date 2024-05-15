package einstein.ambient_sleep.init;

import einstein.ambient_sleep.util.ParticleManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.nextNonAbsDouble;

public class ModDamageListeners {

    public static final Map<EntityType<?>, ParticleManager.EntityProvider<?>> REGISTERED = new HashMap<>();

    public static void init() {
        register(EntityType.CHICKEN, (entity, level, random) -> {
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
        register(EntityType.PARROT, (entity, level, random) -> {
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
        register(EntityType.SNOW_GOLEM, (entity, level, random) -> {
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

    private static <T extends Entity> void register(EntityType<T> type, ParticleManager.EntityProvider<T> provider) {
        REGISTERED.put(type, provider);
    }
}

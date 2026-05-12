package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.EntityAccessor;
import einstein.subtle_effects.util.EntityProvider;
import einstein.subtle_effects.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EntityTickerManager {

    private static final List<EntityTickerProvider<?>> REGISTERED = new ArrayList<>();
    private static final Int2ObjectMap<Int2ObjectMap<EntityTicker<?>>> TRACKED_ENTITIES = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<IntList> TRACKED_ENTITIES_REMOVE_QUEUE = new Int2ObjectOpenHashMap<>();
    public static final int INNER_RANGE = 128;
    public static final int OUTER_RANGE = 144;
    private static int REGISTRATION_ID = 0;
    private static int TICKS_SINCE_LAST_UPDATE = 0;

    public static <T extends Entity> void register(Predicate<Entity> predicate, Function<T, EntityTicker<T>> constructor) {
        REGISTERED.add(new EntityTickerProvider<>(REGISTRATION_ID++, predicate, constructor));
    }

    public static <T extends Entity> void registerSimple(EntityType<T> type, boolean checkVisibility, Supplier<Boolean> isEnabled, EntityProvider<T> provider) {
        registerSimple(entity -> entity.getType().equals(type) && isEnabled.get(), checkVisibility, provider);
    }

    public static <T extends Entity> void registerSimple(Predicate<Entity> predicate, boolean checkVisibility, EntityProvider<T> provider) {
        EntityTickerManager.<T>register(predicate, entity -> new SimpleTicker<>(entity, provider, checkVisibility));
    }

    public static void tick() {
        TICKS_SINCE_LAST_UPDATE++;
        if (TICKS_SINCE_LAST_UPDATE >= ModConfigs.ENTITIES.entityUpdateFrequency.get() + 1) { // Adding 1 so that this happens after updateTickersForEntity has run
            TICKS_SINCE_LAST_UPDATE = 0;

            TRACKED_ENTITIES_REMOVE_QUEUE.forEach((entityId, idsToRemove) -> {
                Int2ObjectMap<EntityTicker<?>> tickers = TRACKED_ENTITIES.get((int) entityId);
                idsToRemove.forEach(id -> {
                    EntityTicker<?> ticker = tickers.get(id);
                    if (ticker != null) {
                        ticker.remove();
                    }

                    tickers.remove(id);
                });

                if (tickers.isEmpty()) {
                    TRACKED_ENTITIES.remove((int) entityId);
                }
            });
            TRACKED_ENTITIES_REMOVE_QUEUE.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void updateTickersForEntity(T entity) {
        int entityId = entity.getId();

        if (!entity.isAlive() || !isEntityInRange(entity, OUTER_RANGE)) {
            removeTickersFromEntity(entity);
            return;
        }

        if (isEntityInRange(entity, INNER_RANGE)) {
            EntityAccessor accessor = (EntityAccessor) entity;
            Int2ObjectMap<EntityTicker<?>> tickers = TRACKED_ENTITIES.get(entityId);

            if (accessor.subtleEffects$isFirstTick() || TICKS_SINCE_LAST_UPDATE >= ModConfigs.ENTITIES.entityUpdateFrequency.get()) {
                for (EntityTickerProvider<?> provider : REGISTERED) {
                    int id = provider.id();
                    boolean areTickersNull = tickers == null;

                    if (areTickersNull || tickers.isEmpty() || !tickers.containsKey(id)) {
                        if (provider.predicate().test(entity)) {
                            EntityTicker<T> ticker = ((EntityTickerProvider<T>) provider).constructor().apply(entity);
                            ticker.setId(id);

                            if (areTickersNull) {
                                tickers = new Int2ObjectOpenHashMap<>();
                                TRACKED_ENTITIES.put(entityId, tickers);
                            }

                            tickers.put(id, ticker);
                            TickerManager.add(ticker);
                        }
                    }
                }
            }
        }
    }

    public static <T extends Entity> boolean isEntityInRange(T entity, int range) {
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            if (player.is(entity)) {
                return true;
            }

            Vec3 position = entity.position();
            return Util.isChunkLoaded(entity.level(), position.x(), position.z()) && player.position().closerThan(position, range);
        }
        return false;
    }

    public static void removeTickersFromEntity(Entity entity) {
        Int2ObjectMap<EntityTicker<?>> tickers = TRACKED_ENTITIES.get(entity.getId());
        if (tickers != null) {
            tickers.forEach((id, ticker) -> removeTickerFromEntity(entity, ticker));
        }
    }

    public static void removeTickerFromEntity(Entity entity, EntityTicker<?> ticker) {
        TRACKED_ENTITIES_REMOVE_QUEUE.computeIfAbsent(entity.getId(), entityId -> new IntArrayList()).add(ticker.getId());
    }

    @Nullable
    public static List<EntityTicker<?>> getTickersForEntity(Entity entity) {
        Int2ObjectMap<EntityTicker<?>> tickers = TRACKED_ENTITIES.get(entity.getId());
        if (tickers != null) {
            return tickers.values().stream().toList();
        }
        return null;
    }

    public static void clear() {
        TRACKED_ENTITIES.clear();
        TRACKED_ENTITIES_REMOVE_QUEUE.clear();
        TICKS_SINCE_LAST_UPDATE = 0;
    }

    public record EntityTickerProvider<T extends Entity>(int id, Predicate<Entity> predicate,
                                                         Function<T, EntityTicker<T>> constructor) {

    }
}

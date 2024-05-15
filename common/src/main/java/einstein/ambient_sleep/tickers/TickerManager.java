package einstein.ambient_sleep.tickers;

import einstein.ambient_sleep.util.EntityProvider;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TickerManager {

    private static final List<TickerProvider<?>> REGISTERED_TICKERS = new ArrayList<>();
    private static final Int2ObjectMap<Int2ObjectMap<Ticker<?>>> TICKERS = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<IntList> REMOVE_QUEUE = new Int2ObjectOpenHashMap<>();
    public static final int INNER_RANGE = 128; // If an entity is outside this range it will stop ticking
    public static final int OUTER_RANGE = 144; // If an entity is outside this range its tickers will be removed
    private static int REGISTERED_TICKER_ID = 0;

    public static <T extends Entity> void registerTicker(Predicate<Entity> predicate, Function<T, Ticker<T>> function) {
        REGISTERED_TICKERS.add(new TickerProvider<>(REGISTERED_TICKER_ID++, predicate, function));
    }

    public static <T extends Entity> void registerSimpleTicker(EntityType<T> type, EntityProvider<T> provider) {
        registerSimpleTicker(entity -> entity.getType().equals(type), provider);
    }

    public static <T extends Entity> void registerSimpleTicker(Predicate<Entity> predicate, EntityProvider<T> provider) {
        REGISTERED_TICKERS.add(new TickerProvider<T>(REGISTERED_TICKER_ID++, predicate, entity -> new SimpleTicker<>(entity, provider)));
    }

    public static void tickTickers(Level level) {
        TICKERS.forEach((entityId, tickers) -> {
            Entity entity = level.getEntity(entityId);
            if (entity != null && entity.isAlive()) {
                if (isEntityInRange(entity, INNER_RANGE)) {
                    IntList removeQueue = new IntArrayList();
                    tickers.forEach((tickerId, ticker) -> {
                        if (ticker.isRemoved()) {
                            removeQueue.add((int) tickerId);
                            return;
                        }

                        ticker.tick();
                    });
                    remove(entityId, removeQueue);
                    return;
                }

                if (!isEntityInRange(entity, OUTER_RANGE)) {
                    remove(entityId, tickers.keySet());
                }
                return;
            }
            remove(entityId, tickers.keySet());
        });

        REMOVE_QUEUE.forEach((entityId, tickerIds) -> {
            if (TICKERS.containsKey((int) entityId)) {
                Int2ObjectMap<Ticker<?>> tickers = TICKERS.get((int) entityId);
                tickerIds.forEach(tickers::remove);

                if (tickers.isEmpty()) {
                    TICKERS.remove((int) entityId);
                }
            }
        });
        REMOVE_QUEUE.clear();
    }

    private static void remove(int entityId, IntCollection tickerIds) {
        if (tickerIds.isEmpty()) {
            return;
        }

        if (REMOVE_QUEUE.containsKey(entityId)) {
            REMOVE_QUEUE.get(entityId).addAll(tickerIds);
            return;
        }

        REMOVE_QUEUE.put(entityId, new IntArrayList(tickerIds));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void createTickersForEntity(T entity) {
        if (getPlayer() != null && isEntityInRange(entity, INNER_RANGE)) {
            int entityId = entity.getId();
            Int2ObjectMap<Ticker<?>> tickers = (TICKERS.containsKey(entityId) ? TICKERS.get(entityId) : new Int2ObjectOpenHashMap<>());

            REGISTERED_TICKERS.forEach(provider -> {
                if (!tickers.containsKey(provider.id())) {
                    if (provider.predicate().test(entity)) {
                        tickers.put(provider.id(), ((TickerProvider<T>) provider).function().apply(entity));
                    }
                }
            });

            if (!tickers.isEmpty()) {
                TICKERS.put(entityId, tickers);
            }
        }
    }

    private static <T extends Entity> boolean isEntityInRange(T entity, int range) {
        return getPlayer().position().closerThan(entity.position(), range);
    }

    public static void clear() {
        TICKERS.clear();
        REMOVE_QUEUE.clear();
    }

    private static @Nullable LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    public record TickerProvider<T extends Entity>(int id, Predicate<Entity> predicate,
                                                   Function<T, Ticker<T>> function) {

    }
}

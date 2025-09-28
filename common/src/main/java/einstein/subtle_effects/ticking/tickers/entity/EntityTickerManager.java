package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.EntityProvider;
import einstein.subtle_effects.util.EntityAccessor;
import einstein.subtle_effects.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EntityTickerManager {

    public static final List<TickerProvider<?>> REGISTERED = new ArrayList<>();
    private static final IntList ENTITIES = new IntArrayList();
    public static final int INNER_RANGE = 128;
    public static final int OUTER_RANGE = 144;
    private static int REGISTRATION_ID = 0;

    public static <T extends Entity> void register(Predicate<Entity> predicate, Function<T, EntityTicker<T>> function) {
        REGISTERED.add(new TickerProvider<>(REGISTRATION_ID++, predicate, function));
    }

    public static <T extends Entity> void registerSimple(EntityType<T> type, boolean checkVisibility, Supplier<Boolean> isEnabled, EntityProvider<T> provider) {
        registerSimple(entity -> entity.getType().equals(type) && isEnabled.get(), checkVisibility, provider);
    }

    public static <T extends Entity> void registerSimple(Predicate<Entity> predicate, boolean checkVisibility, EntityProvider<T> provider) {
        EntityTickerManager.<T>register(predicate, entity -> new SimpleTicker<>(entity, provider, checkVisibility));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void createTickersForEntity(T entity) {
        if (isEntityInRange(entity, INNER_RANGE)) {
            Int2ObjectMap<EntityTicker<?>> tickers = ((EntityAccessor) entity).subtleEffects$getTickers();

            REGISTERED.forEach(provider -> {
                int id = provider.id();
                if (!tickers.containsKey(id)) {
                    if (provider.predicate().test(entity)) {
                        EntityTicker<T> ticker = ((TickerProvider<T>) provider).function().apply(entity);
                        ticker.setId(id);
                        tickers.put(id, ticker);
                        TickerManager.add(ticker);
                        ENTITIES.add(entity.getId());
                    }
                }
            });
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

    public static void clear(@Nullable Level level) {
        if (level != null) {
            ENTITIES.forEach(id -> {
                Entity entity = level.getEntity(id);

                if (entity != null) {
                    ((EntityAccessor) entity).subtleEffects$getTickers().clear();
                }
            });
        }
        ENTITIES.clear();
    }

    public record TickerProvider<T extends Entity>(int id, Predicate<Entity> predicate,
                                                   Function<T, EntityTicker<T>> function) {

    }
}

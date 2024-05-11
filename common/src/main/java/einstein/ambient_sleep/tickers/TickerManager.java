package einstein.ambient_sleep.tickers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TickerManager {

    private static final List<TickerProvider<?>> REGISTERED_TICKERS = new ArrayList<>();
    public static final List<Ticker<?>> TICKERS = new ArrayList<>();
    private static final List<Ticker<?>> REMOVE_QUEUE = new ArrayList<>();

    public static void init() {
        registerTicker(entity -> entity instanceof LivingEntity, SleepingTicker::new);
        registerTicker(entity -> entity instanceof Player, StomachGrowlingTicker::new);
        registerTicker(entity -> entity instanceof Player, MobSkullShaderTicker::new);
    }

    public static void tickTickers() {
        TICKERS.forEach(ticker -> {
            if (ticker.isRemoved()) {
                REMOVE_QUEUE.add(ticker);
                return;
            }

            ticker.tick();
        });
        REMOVE_QUEUE.forEach(TICKERS::remove);
        REMOVE_QUEUE.clear();
    }

    private static <T extends Entity> void registerTicker(Predicate<Entity> predicate, Function<T, Ticker<T>> function) {
        REGISTERED_TICKERS.add(new TickerProvider<>(predicate, function));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void createTickersForEntity(T entity) {
        REGISTERED_TICKERS.forEach(provider -> {
            if (provider.predicate().test(entity)) {
                TICKERS.add(((TickerProvider<T>) provider).function().apply(entity));
            }
        });
    }

    public record TickerProvider<T extends Entity>(Predicate<Entity> predicate,
                                                         Function<T, Ticker<T>> function) {

    }
}

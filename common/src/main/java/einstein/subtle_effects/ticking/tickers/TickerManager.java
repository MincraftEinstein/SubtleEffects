package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.ticking.GeyserManager;
import einstein.subtle_effects.ticking.tickers.entity.EntityTickerManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TickerManager {

    private static final List<Ticker> ADD_QUEUE = new ArrayList<>();
    private static final List<Ticker> TICKERS = new ArrayList<>();
    private static final List<Ticker> REMOVE_QUEUE = new ArrayList<>();

    public static void scheduleNext(Runnable runnable) {
        schedule(1, runnable);
    }

    public static void schedule(int tickDelay, Runnable runnable) {
        add(new ScheduledTicker(tickDelay, runnable));
    }

    public static void add(Ticker ticker) {
        ADD_QUEUE.add(ticker);
    }

    public static void tick() {
        TICKERS.addAll(ADD_QUEUE);
        ADD_QUEUE.clear();

        TICKERS.forEach(ticker -> {
            if (!ticker.isRemoved()) {
                ticker.tick();
                return;
            }

            REMOVE_QUEUE.add(ticker);
        });

        REMOVE_QUEUE.forEach(TICKERS::remove);
        REMOVE_QUEUE.clear();
    }

    public static void clear(@Nullable Level level) {
        ADD_QUEUE.clear();
        TICKERS.clear();
        REMOVE_QUEUE.clear();
        EntityTickerManager.clear(level);
        GeyserManager.ACTIVE_GEYSERS.clear();
        GeyserManager.INACTIVE_GEYSERS.clear();
        WaterfallTicker.WATERFALLS.clear();
        ChestBlockEntityTicker.CHEST_TICKERS.clear();
    }
}

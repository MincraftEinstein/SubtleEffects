package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.ticking.tickers.entity.EntityTicker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.network.chat.CommonComponents.SPACE;

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

    public static void clear() {
        ADD_QUEUE.clear();
        TICKERS.clear();
        REMOVE_QUEUE.clear();
    }

    public static void addDebugInfo(List<Component> lines) {
        int scheduledTickerCount = 0;
        int entityTickerCount = 0;
        int worldTickerCount = 0;
        int playerTickerCount = 0;
        int otherTickerCount = 0;

        for (Ticker ticker : TICKERS) {
            if (!ticker.isRemoved()) {
                switch (ticker) {
                    case ScheduledTicker scheduledTicker -> scheduledTickerCount++;
                    case EntityTicker<?> entityTicker -> {
                        if (entityTicker.entity instanceof Player) {
                            playerTickerCount++;
                            continue;
                        }
                        entityTickerCount++;
                    }
                    case BlockPosTicker blockPosTicker -> worldTickerCount++;
                    default -> otherTickerCount++;
                }
            }
        }

        lines.add(Component.translatable("ui.subtle_effects.debug_overlay.tickers").withStyle(ChatFormatting.GREEN));
        lines.add(Component.empty()
                .append(SPACE)
                .append(Component.translatable("ui.subtle_effects.debug_overlay.tickers.total", TICKERS.size())));

        lines.add(Component.empty()
                .append(SPACE)
                .append(Component.translatable("ui.subtle_effects.debug_overlay.tickers.scheduled", scheduledTickerCount)));

        lines.add(Component.empty()
                .append(SPACE)
                .append(Component.translatable("ui.subtle_effects.debug_overlay.tickers.entity", entityTickerCount)));

        lines.add(Component.empty()
                .append(SPACE)
                .append(Component.translatable("ui.subtle_effects.debug_overlay.tickers.player", playerTickerCount)));

        lines.add(Component.empty()
                .append(SPACE)
                .append(Component.translatable("ui.subtle_effects.debug_overlay.tickers.world", worldTickerCount)));

        lines.add(Component.empty()
                .append(SPACE)
                .append(Component.translatable("ui.subtle_effects.debug_overlay.tickers.other", otherTickerCount)));
    }
}

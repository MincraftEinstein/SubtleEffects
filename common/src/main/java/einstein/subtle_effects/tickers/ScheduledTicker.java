package einstein.subtle_effects.tickers;

public class ScheduledTicker extends Ticker {

    private final int lifeTime;
    private final Runnable runnable;
    private int age;

    public ScheduledTicker(int lifeTime, Runnable runnable) {
        this.lifeTime = lifeTime;
        this.runnable = runnable;
    }

    @Override
    public void tick() {
        age++;

        if (age >= lifeTime) {
            runnable.run();
            remove();
        }
    }
}

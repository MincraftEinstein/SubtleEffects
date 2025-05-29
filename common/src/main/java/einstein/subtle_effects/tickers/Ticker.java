package einstein.subtle_effects.tickers;

public abstract class Ticker {

    private boolean isRemoved;

    public abstract void tick();

    public void remove() {
        isRemoved = true;
    }

    public boolean isRemoved() {
        return isRemoved;
    }
}

package strategy;

public abstract class SortDecorator implements SortStrategy {

    protected final SortStrategy strategy;

    public SortDecorator(SortStrategy strategy) {
        this.strategy = strategy;
    }
}

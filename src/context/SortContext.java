package context;

import strategy.SortStrategy;
import collection.CustomCarList;

public class SortContext {

    private SortStrategy strategy;
    private final CustomCarList carList;

    public SortContext(CustomCarList carList) {
        this.carList = carList;
    }

    public SortContext(CustomCarList carList, SortStrategy strategy) {
        this.carList = carList;
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort() {
        if(strategy == null)
            throw new IllegalStateException("Strategy not set");
        strategy.sort(carList);
    }

}

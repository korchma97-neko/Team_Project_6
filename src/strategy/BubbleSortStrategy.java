package strategy;

import collection.CustomCarList;
import java.util.Comparator;
import model.Car;


public class BubbleSortStrategy implements SortStrategy {

    private final Comparator<Car> comparator;

    public BubbleSortStrategy() {
        this(null);
    }

    public BubbleSortStrategy(Comparator<Car> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void sort(CustomCarList carList) {
        if (carList.size() <= 1) return;

        boolean needIteration = true;

        while (needIteration) {
            needIteration = false;

            for (int i = 1; i < carList.size(); i++) {

                boolean condition;

                if (comparator != null) {
                    condition = comparator.compare(carList.get(i), carList.get(i - 1)) < 0;
                } else {
                    condition = carList.get(i).compareTo(carList.get(i - 1)) < 0;
                }

                if (condition) {
                    Car tmp = carList.get(i);
                    carList.set(i, carList.get(i - 1));
                    carList.set(i - 1, tmp);
                    needIteration = true;
                }
            }
        }
    }
}
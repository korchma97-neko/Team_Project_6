package strategy;

import collection.CustomCarList;
import model.Car;
import java.util.Comparator;

public class SelectionSortStrategy implements SortStrategy {

    private final Comparator<Car> comparator;

    public SelectionSortStrategy() {
        this(null);
    }

    public SelectionSortStrategy(Comparator<Car> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void sort(CustomCarList carList) {

        for (int i = 0; i < carList.size(); i++) {

            int minIndex = i;

            for (int j = i + 1; j < carList.size(); j++) {

                boolean condition;

                if (comparator != null) {
                    condition = comparator.compare(carList.get(j), carList.get(minIndex)) < 0;
                } else {
                    condition = carList.get(j).compareTo(carList.get(minIndex)) < 0;
                }

                if (condition) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {

                Car minCar = carList.get(minIndex);

                while (minIndex > i) {
                    carList.set(minIndex, carList.get(minIndex - 1));
                    minIndex--;
                }

                carList.set(i, minCar);
            }
        }
    }
}

package strategy;
import collection.CustomCarList;


public class EvenOddDecorator extends SortDecorator {

    public EvenOddDecorator(SortStrategy strategy) {
        super(strategy);
    }


    public void sort(CustomCarList carList) {

        if (carList == null || carList.size() < 2) {
            return;
        }

        CustomCarList evenCars = new CustomCarList();

        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getYear() % 2 == 0) {
                evenCars.add(carList.get(i));
            }
        }

        strategy.sort(evenCars);

        int evenIndex = 0;

        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getYear() % 2 == 0) {
                carList.set(i, evenCars.get(evenIndex++));
            }
        }
    }
}

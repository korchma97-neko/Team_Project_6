package tests;
import strategy.*;
import context.*;
import collection.*;
import model.*;
import java.util.Comparator;

public class SortTests {

    public static void main(String[] args) {

        testEvenYearsSorted();
        testOddStayInPlace();
        testAlreadySorted();
        testEmptyList();
        testSingleElement();
        testSortContext();

        testAllOdd();
        testAllEven();
        testSingleEvenAmongOdd();
        testBubbleSortStrategy();

        System.out.println("\nALL TESTS PASSED");
    }

    private static Car createCar(String brand, int year) {
        return new Car.Builder()
                .brand(brand)
                .model("Model")
                .year(year)
                .engineVolume(2.0)
                .color("Black")
                .price(10000)
                .build();
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException("TEST FAILED: " + message);
        }
    }

    private static void testEvenYearsSorted() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2011));
        list.add(createCar("B", 2018));
        list.add(createCar("C", 2014));
        list.add(createCar("D", 2013));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy =
                new EvenOddDecorator(new SelectionSortStrategy(byYear));

        strategy.sort(list);

        assertCondition(list.get(0).getYear() == 2011, "odd element moved");
        assertCondition(list.get(1).getYear() == 2014, "even sorting incorrect");
        assertCondition(list.get(2).getYear() == 2018, "even sorting incorrect");
        assertCondition(list.get(3).getYear() == 2013, "odd element moved");

        System.out.println("testEvenYearsSorted passed");
    }

    private static void testOddStayInPlace() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2011));
        list.add(createCar("B", 2018));
        list.add(createCar("C", 2012));
        list.add(createCar("D", 2015));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy =
                new EvenOddDecorator(new SelectionSortStrategy(byYear));

        strategy.sort(list);

        assertCondition(list.get(0).getYear() == 2011, "first odd moved");
        assertCondition(list.get(3).getYear() == 2015, "last odd moved");

        System.out.println("testOddStayInPlace passed");
    }

    private static void testAlreadySorted() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2010));
        list.add(createCar("B", 2012));
        list.add(createCar("C", 2014));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy = new SelectionSortStrategy(byYear);
        strategy.sort(list);

        assertCondition(list.get(0).getYear() == 2010, "wrong order");
        assertCondition(list.get(1).getYear() == 2012, "wrong order");
        assertCondition(list.get(2).getYear() == 2014, "wrong order");

        System.out.println("testAlreadySorted passed");
    }

    private static void testEmptyList() {

        CustomCarList list = new CustomCarList();

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy = new SelectionSortStrategy(byYear);
        strategy.sort(list);

        assertCondition(list.size() == 0, "empty list changed");

        System.out.println("testEmptyList passed");
    }

    private static void testSingleElement() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2012));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy = new SelectionSortStrategy(byYear);
        strategy.sort(list);

        assertCondition(list.size() == 1, "single element lost");
        assertCondition(list.get(0).getYear() == 2012, "single element changed");

        System.out.println("testSingleElement passed");
    }

    private static void testSortContext() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2018));
        list.add(createCar("B", 2014));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortContext context = new SortContext(list);
        context.setStrategy(new SelectionSortStrategy(byYear));
        context.sort();

        assertCondition(list.get(0).getYear() == 2014, "SortContext failed");

        System.out.println("testSortContext passed");
    }

    private static void testAllOdd() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2011));
        list.add(createCar("B", 2013));
        list.add(createCar("C", 2015));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy =
                new EvenOddDecorator(new SelectionSortStrategy(byYear));

        strategy.sort(list);

        assertCondition(list.get(0).getYear() == 2011, "odd moved");
        assertCondition(list.get(1).getYear() == 2013, "odd moved");
        assertCondition(list.get(2).getYear() == 2015, "odd moved");

        System.out.println("testAllOdd passed");
    }

    private static void testAllEven() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2018));
        list.add(createCar("B", 2012));
        list.add(createCar("C", 2014));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy =
                new EvenOddDecorator(new SelectionSortStrategy(byYear));

        strategy.sort(list);

        assertCondition(list.get(0).getYear() == 2012, "wrong order");
        assertCondition(list.get(1).getYear() == 2014, "wrong order");
        assertCondition(list.get(2).getYear() == 2018, "wrong order");

        System.out.println("testAllEven passed");
    }

    private static void testSingleEvenAmongOdd() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2011));
        list.add(createCar("B", 2013));
        list.add(createCar("C", 2018));
        list.add(createCar("D", 2015));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy =
                new EvenOddDecorator(new SelectionSortStrategy(byYear));

        strategy.sort(list);

        assertCondition(list.get(2).getYear() == 2018, "single even moved");

        System.out.println("testSingleEvenAmongOdd passed");
    }

    private static void testBubbleSortStrategy() {

        CustomCarList list = new CustomCarList();

        list.add(createCar("A", 2018));
        list.add(createCar("B", 2012));
        list.add(createCar("C", 2014));

        Comparator<Car> byYear = Comparator.comparingInt(Car::getYear);

        SortStrategy strategy = new BubbleSortStrategy(byYear);
        strategy.sort(list);

        assertCondition(list.get(0).getYear() == 2012, "bubble sort failed");
        assertCondition(list.get(1).getYear() == 2014, "bubble sort failed");
        assertCondition(list.get(2).getYear() == 2018, "bubble sort failed");

        System.out.println("testBubbleSortStrategy passed");
    }

}


package test;

import model.Car;
import collection.CustomCarList;
import strategy.BubbleSortStrategy;
import strategy.SelectionSortStrategy;
import strategy.EvenOddDecorator;
import context.SortContext;
import service.MultiThreadCounter;

import java.util.Comparator;

public class CarTest {

    public static void main(String[] args) {
        System.out.println("=== ЗАПУСК ТЕСТОВ ===\n");

        testCarBuilder();
        testCustomCarList();
        testSortingStrategies();
        testEvenOddDecorator();
        testMultiThreadCounter();

        System.out.println("\n=== ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ===");
    }

    private static void testCarBuilder() {
        System.out.println("Тест 1: Car Builder");

        // Тест успешного создания
        try {
            Car car = new Car.Builder()
                    .brand("Toyota")
                    .model("Camry")
                    .year(2020)
                    .build();
            System.out.println("  ✓ Успешное создание: " + car);
        } catch (Exception e) {
            System.out.println("  ✗ Ошибка: " + e.getMessage());
        }

        // Тест валидации (пустой бренд)
        try {
            Car car = new Car.Builder()
                    .brand("")
                    .model("Camry")
                    .year(2020)
                    .build();
            System.out.println("  ✗ Должна быть ошибка валидации");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ Корректная валидация пустого бренда: " + e.getMessage());
        }

        // Тест валидации (год)
        try {
            Car car = new Car.Builder()
                    .brand("Toyota")
                    .model("Camry")
                    .year(1800)
                    .build();
            System.out.println("  ✗ Должна быть ошибка валидации года");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ Корректная валидация года: " + e.getMessage());
        }

        System.out.println();
    }

    private static void testCustomCarList() {
        System.out.println("Тест 2: CustomCarList");

        CustomCarList list = new CustomCarList();

        // Тест добавления
        Car car1 = createTestCar("Toyota", "Camry", 2020);
        Car car2 = createTestCar("Honda", "Accord", 2021);

        list.add(car1);
        list.add(car2);

        if (list.size() == 2) {
            System.out.println("  ✓ Добавление элементов работает");
        } else {
            System.out.println("  ✗ Ошибка добавления");
        }

        // Тест получения
        if (list.get(0).equals(car1)) {
            System.out.println("  ✓ Получение элемента работает");
        } else {
            System.out.println("  ✗ Ошибка получения элемента");
        }

        // Тест удаления
        Car removed = list.remove(0);
        if (removed.equals(car1) && list.size() == 1) {
            System.out.println("  ✓ Удаление элемента работает");
        } else {
            System.out.println("  ✗ Ошибка удаления элемента");
        }

        // Тест contains
        if (list.contains(car2) && !list.contains(car1)) {
            System.out.println("  ✓ Contains работает");
        } else {
            System.out.println("  ✗ Ошибка contains");
        }

        // Тест clear
        list.clear();
        if (list.isEmpty()) {
            System.out.println("  ✓ Clear работает");
        } else {
            System.out.println("  ✗ Ошибка clear");
        }

        System.out.println();
    }

    private static void testSortingStrategies() {
        System.out.println("Тест 3: Стратегии сортировки");

        CustomCarList list = new CustomCarList();
        list.add(createTestCar("BMW", "X5", 2021));
        list.add(createTestCar("Audi", "A4", 2020));
        list.add(createTestCar("Mercedes", "C-Class", 2019));

        System.out.println("  Исходный список:");
        list.forEach(car -> System.out.println("    " + car));

        // Тест BubbleSort по году
        SortContext context = new SortContext(list);
        context.setStrategy(new BubbleSortStrategy(Comparator.comparingInt(Car::getYear)));
        context.sort();

        boolean bubbleSorted = list.get(0).getYear() == 2019 &&
                list.get(1).getYear() == 2020 &&
                list.get(2).getYear() == 2021;

        if (bubbleSorted) {
            System.out.println("  ✓ BubbleSort работает корректно");
        } else {
            System.out.println("  ✗ Ошибка BubbleSort");
        }

        // Тест SelectionSort по бренду
        list.clear();
        list.add(createTestCar("BMW", "X5", 2021));
        list.add(createTestCar("Audi", "A4", 2020));
        list.add(createTestCar("Mercedes", "C-Class", 2019));

        context.setStrategy(new SelectionSortStrategy(Comparator.comparing(Car::getBrand)));
        context.sort();

        if (list.get(0).getBrand().equals("Audi") &&
                list.get(1).getBrand().equals("BMW") &&
                list.get(2).getBrand().equals("Mercedes")) {
            System.out.println("  ✓ SelectionSort работает корректно");
        } else {
            System.out.println("  ✗ Ошибка SelectionSort");
        }

        System.out.println();
    }

    private static void testEvenOddDecorator() {
        System.out.println("Тест 4: EvenOddDecorator");

        CustomCarList list = new CustomCarList();
        list.add(createTestCar("Car1", "Model1", 2021)); // нечетный
        list.add(createTestCar("Car2", "Model2", 2020)); // четный
        list.add(createTestCar("Car3", "Model3", 2019)); // нечетный
        list.add(createTestCar("Car4", "Model4", 2018)); // четный
        list.add(createTestCar("Car5", "Model5", 2022)); // четный

        // Запоминаем позиции нечетных
        Car odd1 = list.get(0);
        Car odd2 = list.get(2);

        SortContext context = new SortContext(list);
        context.setStrategy(new EvenOddDecorator(
                new BubbleSortStrategy(Comparator.comparingInt(Car::getYear))
        ));
        context.sort();

        // Проверяем, что нечетные остались на своих местах
        boolean oddPositionsOk = list.get(0).equals(odd1) &&
                list.get(2).equals(odd2);

        // Проверяем, что четные отсортированы (2018, 2020, 2022)
        boolean evenSorted = list.get(1).getYear() == 2018 &&
                list.get(3).getYear() == 2020 &&
                list.get(4).getYear() == 2022;

        if (oddPositionsOk && evenSorted) {
            System.out.println("  ✓ EvenOddDecorator работает корректно");
        } else {
            System.out.println("  ✗ Ошибка EvenOddDecorator");
        }

        System.out.println();
    }

    private static void testMultiThreadCounter() {
        System.out.println("Тест 5: Многопоточный счетчик");

        CustomCarList list = new CustomCarList();
        list.add(createTestCar("Car1", "Model1", 2020));
        list.add(createTestCar("Car2", "Model2", 2021));
        list.add(createTestCar("Car3", "Model3", 2020));
        list.add(createTestCar("Car4", "Model4", 2019));
        list.add(createTestCar("Car5", "Model5", 2020));

        MultiThreadCounter counter = new MultiThreadCounter(3);

        int count2020 = counter.countCarsByYear(list, 2020);
        if (count2020 == 3) {
            System.out.println("  ✓ Подсчет 2020 года работает (ожидалось 3, получено " + count2020 + ")");
        } else {
            System.out.println("  ✗ Ошибка подсчета 2020 года (ожидалось 3, получено " + count2020 + ")");
        }

        int count2021 = counter.countCarsByYear(list, 2021);
        if (count2021 == 1) {
            System.out.println("  ✓ Подсчет 2021 года работает (ожидалось 1, получено " + count2021 + ")");
        } else {
            System.out.println("  ✗ Ошибка подсчета 2021 года (ожидалось 1, получено " + count2021 + ")");
        }

        int count2022 = counter.countCarsByYear(list, 2022);
        if (count2022 == 0) {
            System.out.println("  ✓ Подсчет 2022 года работает (ожидалось 0, получено " + count2022 + ")");
        } else {
            System.out.println("  ✗ Ошибка подсчета 2022 года (ожидалось 0, получено " + count2022 + ")");
        }

        System.out.println();
    }

    private static Car createTestCar(String brand, String model, int year) {
        try {
            return new Car.Builder()
                    .brand(brand)
                    .model(model)
                    .year(year)
                    .build();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
package app;

import collection.CustomCarList;
import model.Car;
import context.SortContext;
import strategy.BubbleSortStrategy;
import strategy.EvenOddDecorator;
import strategy.SelectionSortStrategy;
import strategy.SortStrategy;
import service.MultiThreadCounter;
import service.CounterService;

import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CustomCarList carList = new CustomCarList();
    private static final CounterService counterService = new MultiThreadCounter(3); // 3 как в тесте

    public static void main(String[] args) {

        new Main().run(); //Запуск главного цикла программы
}

    private void run() {    //Главный цыкл программы
        System.out.println("=== Программа сортировки автомобилей ===");

        // Добавим несколько тестовых автомобилей для демонстрации
        initializeTestData();

        while (true) {
            printMenu();
            int choice = readIntInput();

            switch (choice) {
                case 1:
                    // Показать все автомобили
                    showAllCars();
                    break;
                case 2:
                    // Добавить автомобиль
                    addNewCar();
                    break;
                case 3:
                    // Удалить автомобиль
                    deleteCar();
                    break;
                case 4:
                    // Сортировать автомобили
                    sortCars();
                    break;
                case 5:
                    // Поиск автомобиля
                    searchCar();
                    break;
                case 6:
                    // Фильтрация через Stream API
                    filterCars();
                    break;
                case 7:
                    // Информация о коллекции
                    showCollectionInfo();
                    break;
                case 8:
                    // Многопоточность
                    countCarsByYearMultiThreaded();
                    break;
                case 0:
                    System.out.println("Программа завершена. До свидания!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }



    private void printMainMenu() {
        System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
        System.out.println("1. Инициализировать коллекцию (заполнить данными)");
        System.out.println("2. Показать все автомобили");
        System.out.println("3. Добавить автомобиль (вручную)");
        System.out.println("4. Удалить автомобиль");
        System.out.println("5. Сортировать автомобили");
        System.out.println("6. Поиск автомобиля");
        System.out.println("7. Фильтрация через Stream API");
        System.out.println("8. Работа с файлами");
        System.out.println("9. Информация о коллекции");
        System.out.println("10. Многопоточный подсчёт автомобилей по году");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void showAllCars() {
        System.out.println("\n=== Список автомобилей ===");
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        for (int i = 0; i < carList.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, carList.get(i));
        }
        System.out.printf("Всего автомобилей: %d%n", carList.size());
    }

    private static void addNewCar() {
        System.out.println("\n=== Добавление нового автомобиля ===");

        try {
            Car.Builder builder = new Car.Builder();

            System.out.print("Введите бренд: ");
            builder.brand(scanner.nextLine());

            System.out.print("Введите модель: ");
            builder.model(scanner.nextLine());

            System.out.print("Введите год выпуска: ");
            builder.year(readIntInput());

            Car newCar = builder.build();
            carList.add(newCar);
            System.out.println("Автомобиль успешно добавлен!");

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании автомобиля: " + e.getMessage());
        }
    }

    private static void deleteCar() {
        System.out.println("\n=== Удаление автомобиля ===");
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        showAllCars();
        System.out.print("Введите номер автомобиля для удаления: ");
        int index = readIntInput() - 1;

        try {
            Car removed = carList.remove(index);
            System.out.println("Удален автомобиль: " + removed.getBrand() + " " + removed.getModel());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Неверный номер автомобиля!");
        }
    }

    private static void sortCars() {
        System.out.println("\n=== Сортировка автомобилей ===");
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        System.out.println("\n=== Сортировка ===");
        System.out.println("1. По бренду (естественный порядок)");
        System.out.println("2. По году выпуска");
        System.out.println("3. По модели (естественный порядок)");
        System.out.println("0. Назад");
        System.out.print("Выберите тип сортировки: ");

        int choice = readIntInput();

        Comparator<Car> comparator;
        switch (choice) {
            case 1:
                comparator = Comparator.comparing(Car::getBrand, String.CASE_INSENSITIVE_ORDER);
                System.out.println("Отсортировано по бренду (естественный порядок)");
                break;
            case 2:
                comparator = Comparator.comparingInt(Car::getYear);
                System.out.println("Отсортировано по году выпуска");
                break;
            case 3:
                comparator = Comparator.comparing(Car::getModel, String.CASE_INSENSITIVE_ORDER);
                System.out.println("Отсортировано по модели (естественный порядок)");
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        System.out.println("\n=== Выбор алгоритма ===");
        System.out.println("1. SelectionSort");
        System.out.println("2. BubbleSort");
        System.out.println("0. Назад");
        System.out.print("Выберите алгоритм сортировки: ");

        int algChoice = readIntInput();

        SortStrategy strategy;
        switch (algChoice) {
            case 1:
                strategy = new SelectionSortStrategy(comparator);
                System.out.println("Выбран алгоритм Selection");
                break;
            case 2:
                strategy = new BubbleSortStrategy(comparator);
                System.out.println("Выбран алгоритм Bubble");
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор!");
                return;
        }


        if (choice == 2) {  // EvenOddDecorator только для сортировки по году
            System.out.println("\n=== Режим сортировки ===");
            System.out.println("1. Обычная сортировка по году");
            System.out.println("2. Сортировать только автомобили с четным годом");
            System.out.println("0. Назад");
            System.out.print("Выберите режим: ");

            int modeChoice = readIntInput();

            switch (modeChoice) {
                case 1:
                    break;
                case 2:
                    strategy = new EvenOddDecorator(strategy);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор!");
                    return;
            }
        }
        SortContext context = new SortContext(carList);
        context.setStrategy(strategy);
        context.sort();

        showAllCars();
        System.out.println("Сортировка выполнена");
    }

    private static void searchCar() {
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        System.out.println("\n=== Поиск автомобиля ===");
        System.out.print("Введите бренд для поиска: ");
        String brand = scanner.nextLine();

        boolean found = false;
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            if (car.getBrand().equalsIgnoreCase(brand)) {
                System.out.printf("Найден: %s%n", car);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Автомобили бренда " + brand + " не найдены");
        }
    }

    private static void filterCars() {
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        System.out.println("\n=== Фильтрация через Stream API ===");
        System.out.println("1. Показать автомобили указанного бренда");
        System.out.println("2. Показать автомобили новее указанного года");
        System.out.println("3. Показать автомобили старше указанного года");
        System.out.println("0. Назад");
        System.out.print("Выберите фильтр: ");

        int choice = readIntInput();

        CustomCarList filtered = new CustomCarList();

        switch (choice) {
            case 1:
                System.out.print("Введите бренд: ");
                String brand = readStringInput();
                filtered = carList.filter(car -> car.getBrand().equalsIgnoreCase(brand));
                System.out.println("Автомобили бренда " + brand + ":");
                break;
            case 2:
                System.out.print("Введите год: ");
                int newerYear = readIntInput();
                filtered = carList.filter(car -> car.getYear() > newerYear);
                System.out.println("Автомобили новее " + newerYear + " года:");
                break;
            case 3:
                System.out.print("Введите год: ");
                int olderYear = readIntInput();
                filtered = carList.filter(car -> car.getYear() < olderYear);
                System.out.println("Автомобили старше " + olderYear + " года:");
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        // Используем Stream API для вывода
        filtered.stream().forEach(car -> System.out.println("  " + car));
        System.out.println("Найдено: " + filtered.size() + " автомобилей");
    }

    private static void showCollectionInfo() {
        System.out.println("\n=== Информация о коллекции ===");
        System.out.println("Размер коллекции: " + carList.size());
        System.out.println("Пустая? " + (carList.isEmpty() ? "Да" : "Нет"));

        if (!carList.isEmpty()) {
            int minYear = carList.stream()
                    .mapToInt(Car::getYear)
                    .min()
                    .orElse(0);

            int maxYear = carList.stream()
                    .mapToInt(Car::getYear)
                    .max()
                    .orElse(0);

            long toyotaCount = carList.stream()
                    .filter(car -> "Toyota".equalsIgnoreCase(car.getBrand()))
                    .count();

            System.out.println("Годы выпуска: от " + minYear + " до " + maxYear);
            System.out.println("Количество Toyota: " + toyotaCount);

            System.out.println("\nПеребор с помощью итератора:");
            for (Car car : carList) {
                System.out.println("  " + car.getBrand() + " " + car.getModel());
            }
        }
    }

    private static void countCarsByYearMultiThreaded() {
        System.out.println("\n=== Многопоточный подсчёт автомобилей по году ===");

        if (carList.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Введите год для подсчёта: ");
        int year = readIntInput();
        int result = counterService.countCarsByYear(carList, year);

        System.out.println("Количество автомобилей с годом " + year + ": " + result);
    }

    private static void initializeTestData() {
        try {
            carList.add(new Car.Builder()
                    .brand("Toyota")
                    .model("Camry")
                    .year(2020)
                    .build());

            carList.add(new Car.Builder()
                    .brand("BMW")
                    .model("X5")
                    .year(2021)
                    .build());

            carList.add(new Car.Builder()
                    .brand("Honda")
                    .model("Accord")
                    .year(2019)
                    .build());

            carList.add(new Car.Builder()
                    .brand("Toyota")
                    .model("RAV4")
                    .year(2022)
                    .build());

            carList.add(new Car.Builder()
                    .brand("Mercedes")
                    .model("E-Class")
                    .year(2020)
                    .build());

            System.out.println("Добавлены тестовые данные (" + carList.size() + " автомобилей)");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании тестовых данных: " + e.getMessage());
        }
    }

    // Проверки на пользовательскую ошибку ввода
    private static int readIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите число: ");
            }
        }
    }

    private static String readStringInput() {
        while (true) {
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Ошибка! Cтрока не должна быть пустой.");
        }
    }
}
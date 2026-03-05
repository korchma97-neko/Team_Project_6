import collection.CustomCarList;
import model.Car;

import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CustomCarList carList = new CustomCarList();

    public static void main(String[] args) {
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
                case 0:
                    System.out.println("Программа завершена. До свидания!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Показать все автомобили");
        System.out.println("2. Добавить новый автомобиль");
        System.out.println("3. Удалить автомобиль");
        System.out.println("4. Сортировать автомобили");
        System.out.println("5. Поиск автомобиля");
        System.out.println("6. Фильтрация через Stream API");
        System.out.println("7. Информация о коллекции");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void showAllCars() {
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        System.out.println("\n=== Список автомобилей ===");
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

            System.out.print("Введите объем двигателя (литры): ");
            builder.engineVolume(readDoubleInput());

            System.out.print("Введите цвет: ");
            builder.color(scanner.nextLine());

            System.out.print("Введите цену: ");
            builder.price(readIntInput());

            Car newCar = builder.build();
            carList.add(newCar);
            System.out.println("Автомобиль успешно добавлен!");

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании автомобиля: " + e.getMessage());
        }
    }

    private static void deleteCar() {
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
        if (carList.isEmpty()) {
            System.out.println("Список автомобилей пуст!");
            return;
        }

        System.out.println("\n=== Сортировка ===");
        System.out.println("1. По бренду (естественный порядок)");
        System.out.println("2. По году выпуска");
        System.out.println("3. По цене (возрастание)");
        System.out.println("4. По цене (убывание)");
        System.out.println("5. По объему двигателя");
        System.out.println("0. Назад");
        System.out.print("Выберите тип сортировки: ");

        int choice = readIntInput();

        switch (choice) {
            case 1:
                // Естественный порядок (из compareTo в Car)
                carList.sort();
                System.out.println("Отсортировано по бренду, модели и году (естественный порядок)");
                break;
            case 2:
                carList.sort(Comparator.comparingInt(Car::getYear));
                System.out.println("Отсортировано по году выпуска");
                break;
            case 3:
                carList.sort(Comparator.comparingInt(Car::getPrice));
                System.out.println("Отсортировано по цене (возрастание)");
                break;
            case 4:
                carList.sort(Comparator.comparingInt(Car::getPrice).reversed());
                System.out.println("Отсортировано по цене (убывание)");
                break;
            case 5:
                carList.sort(Comparator.comparingDouble(Car::getEngineVolume));
                System.out.println("Отсортировано по объему двигателя");
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        showAllCars();
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
        System.out.println("1. Автомобили дороже 30000");
        System.out.println("2. Автомобили новее 2020 года");
        System.out.println("3. Автомобили с объемом двигателя больше 2.5");
        System.out.println("4. Только Toyota");
        System.out.println("0. Назад");
        System.out.print("Выберите фильтр: ");

        int choice = readIntInput();

        CustomCarList filtered = new CustomCarList();

        switch (choice) {
            case 1:
                filtered = carList.filter(car -> car.getPrice() > 30000);
                System.out.println("Автомобили дороже 30000:");
                break;
            case 2:
                filtered = carList.filter(car -> car.getYear() > 2020);
                System.out.println("Автомобили новее 2020 года:");
                break;
            case 3:
                filtered = carList.filter(car -> car.getEngineVolume() > 2.5);
                System.out.println("Автомобили с объемом > 2.5:");
                break;
            case 4:
                filtered = carList.filter(car -> "Toyota".equals(car.getBrand()));
                System.out.println("Автомобили Toyota:");
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
            // Используем Stream API для подсчета статистики
            double avgPrice = carList.stream()
                    .mapToInt(Car::getPrice)
                    .average()
                    .orElse(0);

            int minYear = carList.stream()
                    .mapToInt(Car::getYear)
                    .min()
                    .orElse(0);

            int maxYear = carList.stream()
                    .mapToInt(Car::getYear)
                    .max()
                    .orElse(0);

            long toyotaCount = carList.stream()
                    .filter(car -> "Toyota".equals(car.getBrand()))
                    .count();

            System.out.printf("Средняя цена: %.2f%n", avgPrice);
            System.out.println("Годы выпуска: от " + minYear + " до " + maxYear);
            System.out.println("Количество Toyota: " + toyotaCount);

            // Демонстрация итератора
            System.out.println("\nПеребор с помощью итератора:");
            for (Car car : carList) {
                System.out.println("  " + car.getBrand() + " " + car.getModel());
            }
        }
    }

    private static void initializeTestData() {
        try {
            carList.add(new Car.Builder()
                    .brand("Toyota")
                    .model("Camry")
                    .year(2020)
                    .engineVolume(2.5)
                    .color("Black")
                    .price(25000)
                    .build());

            carList.add(new Car.Builder()
                    .brand("BMW")
                    .model("X5")
                    .year(2021)
                    .engineVolume(3.0)
                    .color("White")
                    .price(60000)
                    .build());

            carList.add(new Car.Builder()
                    .brand("Honda")
                    .model("Accord")
                    .year(2019)
                    .engineVolume(2.0)
                    .color("Red")
                    .price(22000)
                    .build());

            carList.add(new Car.Builder()
                    .brand("Toyota")
                    .model("RAV4")
                    .year(2022)
                    .engineVolume(2.5)
                    .color("Blue")
                    .price(32000)
                    .build());

            carList.add(new Car.Builder()
                    .brand("Mercedes")
                    .model("E-Class")
                    .year(2020)
                    .engineVolume(2.0)
                    .color("Silver")
                    .price(45000)
                    .build());

            System.out.println("Добавлены тестовые данные (" + carList.size() + " автомобилей)");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании тестовых данных: " + e.getMessage());
        }
    }

    private static int readIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите число: ");
            }
        }
    }

    private static double readDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите число (например, 2.5): ");
            }
        }
    }
}
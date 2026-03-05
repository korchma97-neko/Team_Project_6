package tests;

import model.Car;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuilderTests {

    public static void main(String[] args) {
        System.out.println("=== Тестирование Builder паттерна и Car модели ===\n");

        testBuilderPattern();
        testValidation();
        testComparable();
        testToString();
    }

    private static void testBuilderPattern() {
        System.out.println("1. Тестирование Builder паттерна:");
        System.out.println("--------------------------------");

        // Создание автомобиля с помощью Builder
        try {
            Car car1 = new Car.Builder()
                    .brand("Toyota")
                    .model("Camry")
                    .year(2024)
                    .engineVolume(2.5)
                    .color("Silver")
                    .price(35000)
                    .build();

            Car car2 = new Car.Builder()
                    .brand("BMW")
                    .model("X5")
                    .year(2023)
                    .engineVolume(3.0)
                    .color("Black")
                    .price(75000)
                    .build();

            System.out.println("Car 1: " + car1);
            System.out.println("Car 2: " + car2);
            System.out.println("✓ Builder успешно создал объекты Car\n");

        } catch (Exception e) {
            System.out.println("✗ Ошибка при создании объектов: " + e.getMessage() + "\n");
        }
    }

    private static void testValidation() {
        System.out.println("2. Тестирование валидации:");
        System.out.println("---------------------------");

        // Тест 1: Пустой brand
        try {
            new Car.Builder()
                    .brand("")
                    .model("Civic")
                    .year(2022)
                    .engineVolume(1.8)
                    .color("Red")
                    .price(25000)
                    .build();
            System.out.println("✗ Должна быть ошибка для пустого brand");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Корректно поймана ошибка: " + e.getMessage());
        }

        // Тест 2: Неверный год
        try {
            new Car.Builder()
                    .brand("Honda")
                    .model("Civic")
                    .year(1899)
                    .engineVolume(1.8)
                    .color("Red")
                    .price(25000)
                    .build();
            System.out.println("✗ Должна быть ошибка для некорректного года");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Корректно поймана ошибка: " + e.getMessage());
        }

        // Тест 3: Неверный объем двигателя
        try {
            new Car.Builder()
                    .brand("Honda")
                    .model("Civic")
                    .year(2022)
                    .engineVolume(11.0)
                    .color("Red")
                    .price(25000)
                    .build();
            System.out.println("✗ Должна быть ошибка для некорректного объема двигателя");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Корректно поймана ошибка: " + e.getMessage());
        }

        // Тест 4: Отрицательная цена
        try {
            new Car.Builder()
                    .brand("Honda")
                    .model("Civic")
                    .year(2022)
                    .engineVolume(1.8)
                    .color("Red")
                    .price(-1000)
                    .build();
            System.out.println("✗ Должна быть ошибка для отрицательной цены");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Корректно поймана ошибка: " + e.getMessage());
        }

        System.out.println();
    }

    private static void testComparable() {
        System.out.println("3. Тестирование Comparable (сортировка по brand, year, price):");
        System.out.println("-------------------------------------------------------------");

        // Создаем список автомобилей
        List<Car> cars = Arrays.asList(
                new Car.Builder().brand("Toyota").model("Camry").year(2022).engineVolume(2.5).color("Silver").price(35000).build(),
                new Car.Builder().brand("Audi").model("A4").year(2021).engineVolume(2.0).color("White").price(40000).build(),
                new Car.Builder().brand("BMW").model("X3").year(2023).engineVolume(2.0).color("Black").price(45000).build(),
                new Car.Builder().brand("Audi").model("Q5").year(2022).engineVolume(2.0).color("Blue").price(42000).build(),
                new Car.Builder().brand("Audi").model("A4").year(2022).engineVolume(2.0).color("Red").price(38000).build(),
                new Car.Builder().brand("Toyota").model("Corolla").year(2022).engineVolume(1.8).color("Gray").price(25000).build()
        );

        System.out.println("До сортировки:");
        cars.forEach(System.out::println);

        // Сортировка с использованием compareTo()
        Collections.sort(cars);

        System.out.println("\nПосле сортировки (по brand -> year -> price):");
        cars.forEach(System.out::println);

        // Проверка порядка сортировки
        boolean correctOrder = true;
        for (int i = 0; i < cars.size() - 1; i++) {
            if (cars.get(i).compareTo(cars.get(i + 1)) > 0) {
                correctOrder = false;
                break;
            }
        }

        System.out.println("\n✓ Порядок сортировки корректен: " + correctOrder + "\n");
    }

    private static void testToString() {
        System.out.println("4. Тестирование toString():");
        System.out.println("--------------------------");

        Car car = new Car.Builder()
                .brand("Tesla")
                .model("Model 3")
                .year(2023)
                .engineVolume(0.0)
                .color("Red")
                .price(50000)
                .build();

        System.out.println("toString output: " + car);
        System.out.println("✓ toString() успешно реализован\n");
    }
}

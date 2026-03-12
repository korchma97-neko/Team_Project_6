package io;

import model.Car;
import collection.CustomCarList;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class FileManager {
    private static final String DATA_DIR = "data";
    private static final String DEFAULT_FILE = DATA_DIR + "/cars.txt";

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Не удалось создать директорию data: " + e.getMessage());
        }
    }

    public static void saveToFile(CustomCarList cars, String filename, boolean append) {
        try {
            Path path = Paths.get(DATA_DIR, filename);
            if (!append && Files.exists(path)) {
                Files.delete(path);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    append ? StandardOpenOption.CREATE : StandardOpenOption.CREATE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)) {

                for (Car car : cars) {
                    writer.write(String.format("%s|%s|%d%n",
                            car.getBrand(), car.getModel(), car.getYear()));
                }
            }
            System.out.println("Данные сохранены в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    public static CustomCarList loadFromFile(String filename) {
        CustomCarList cars = new CustomCarList();
        Path path = Paths.get(DATA_DIR, filename);

        if (!Files.exists(path)) {
            System.out.println("Файл не найден: " + filename);
            return cars;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String[] parts = line.split("\\|");
                    if (parts.length != 3) {
                        System.err.println("Строка " + lineNumber + ": неверный формат, пропущена");
                        continue;
                    }

                    String brand = parts[0].trim();
                    String model = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());

                    Car car = new Car.Builder()
                            .brand(brand)
                            .model(model)
                            .year(year)
                            .build();
                    cars.add(car);
                } catch (IllegalArgumentException e) {
                    System.err.println("Строка " + lineNumber + ": ошибка валидации - " + e.getMessage());
                }
            }
            System.out.println("Загружено " + cars.size() + " автомобилей из файла " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return cars;
    }

    public static List<String> listAvailableFiles() {
        List<String> files = new ArrayList<>();
        try {
            Files.list(Paths.get(DATA_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(path -> files.add(path.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Ошибка при чтении директории: " + e.getMessage());
        }
        return files;
    }
}

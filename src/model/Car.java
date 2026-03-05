package model;

import java.util.Objects;

public class Car implements Comparable<Car> {
    // Поля класса
    private final String brand;
    private final String model;
    private final int year;
    private final double engineVolume;
    private final String color;
    private final int price;

    // Приватный конструктор, использующий Builder
    private Car(Builder builder) {
        this.brand = builder.brand;
        this.model = builder.model;
        this.year = builder.year;
        this.engineVolume = builder.engineVolume;
        this.color = builder.color;
        this.price = builder.price;
    }

    // Геттеры (для доступа к полям)
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {

        return year;
    }

    public double getEngineVolume() {
        return engineVolume;
    }

    public String getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int compareTo(Car other) {
        // Сравниваем по бренду
        int brandCompare = this.brand.compareTo(other.brand);
        if (brandCompare != 0) {
            return brandCompare;
        }

        // Если бренды одинаковые, сравниваем по модели
        int modelCompare = this.model.compareTo(other.model);
        if (modelCompare != 0) {
            return modelCompare;
        }

        // Если бренд и модель одинаковые, сравниваем по году
        return Integer.compare(this.year, other.year);
    }

    @Override
    public String toString() {
        return String.format("Car{brand='%s', model='%s', year=%d, engine=%.1fL, color='%s', price=%d}",
                brand, model, year, engineVolume, color, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return year == car.year &&
                Double.compare(car.engineVolume, engineVolume) == 0 &&
                price == car.price &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model) &&
                Objects.equals(color, car.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, year, engineVolume, color, price);
    }

        // Внутренний статический класс Builder для построения объектов Car

    public static class Builder {
        // Поля Builder соответствуют полям Car
        private String brand;
        private String model;
        private int year;
        private double engineVolume;
        private String color;
        private int price;

        // Методы-сеттеры, возвращающие this для цепочечных вызовов
        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder engineVolume(double engineVolume) {
            this.engineVolume = engineVolume;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        // Метод build() создает объект Car после валидации всех полей
        public Car build() {
            validate();
            return new Car(this);
        }

        // Приватный метод для валидации всех полей
        private void validate() {

            // Проверка бренда
            if (brand == null || brand.trim().isEmpty()) {
                throw new IllegalArgumentException("Brand cannot be null or empty");
            }
            if (brand.length() > 50) {
                throw new IllegalArgumentException("Brand name is too long (max 50 characters)");
            }

            // Проверка модели
            if (model == null || model.trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be null or empty");
            }
            if (model.length() > 50) {
                throw new IllegalArgumentException("Model name is too long (max 50 characters)");
            }

            // Проверка года выпуска
            // Первый автомобиль был создан в 1886 году (Carl Benz)
            if (year < 1886 || year > 2025) {
                throw new IllegalArgumentException("Year must be between 1886 and 2025");
            }

            // Проверка объема двигателя
            if (engineVolume <= 0 || engineVolume > 20.0) {
                throw new IllegalArgumentException("Engine volume must be between 0.1 and 20.0 liters");
            }

            // Проверка цвета
            if (color == null || color.trim().isEmpty()) {
                throw new IllegalArgumentException("Color cannot be null or empty");
            }
            if (color.length() > 30) {
                throw new IllegalArgumentException("Color name is too long (max 30 characters)");
            }

            // Проверка цены
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }

            // Лимит для автомобиля
            if (price > 1_000_000) {
                throw new IllegalArgumentException("Price seems too high (max 1,000,000)");
            }
        }
    }
}
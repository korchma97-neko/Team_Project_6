package tests;

import collection.CustomCarList;
import model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для CustomCarList
 */
@DisplayName("Тесты кастомной коллекции CustomCarList")
public class CollectionTests {

    private CustomCarList emptyList;
    private CustomCarList listWithCars;
    private Car car1;
    private Car car2;
    private Car car3;
    private Car car4;

    @BeforeEach
    void setUp() {
        emptyList = new CustomCarList();

        // Создаем тестовые автомобили с помощью Builder
        car1 = new Car.Builder()
                .brand("Toyota")
                .model("Camry")
                .year(2020)
                .engineVolume(2.5)
                .color("Black")
                .price(25000)
                .build();

        car2 = new Car.Builder()
                .brand("BMW")
                .model("X5")
                .year(2021)
                .engineVolume(3.0)
                .color("White")
                .price(60000)
                .build();

        car3 = new Car.Builder()
                .brand("Honda")
                .model("Accord")
                .year(2019)
                .engineVolume(2.0)
                .color("Red")
                .price(22000)
                .build();

        car4 = new Car.Builder()
                .brand("Toyota")
                .model("Camry")
                .year(2022)
                .engineVolume(2.5)
                .color("Blue")
                .price(27000)
                .build();

        listWithCars = new CustomCarList();
        listWithCars.add(car1);
        listWithCars.add(car2);
        listWithCars.add(car3);
    }

    @Nested
    @DisplayName("Тесты конструкторов")
    class ConstructorTests {

        @Test
        @DisplayName("Конструктор по умолчанию создает пустой список")
        void defaultConstructorCreatesEmptyList() {
            CustomCarList list = new CustomCarList();
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
        }

        @Test
        @DisplayName("Конструктор с начальной емкостью создает список с указанной емкостью")
        void constructorWithInitialCapacity() {
            CustomCarList list = new CustomCarList(20);
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
        }

        @Test
        @DisplayName("Конструктор с отрицательной емкостью выбрасывает исключение")
        void constructorWithNegativeCapacityThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> new CustomCarList(-5));
        }

        @Test
        @DisplayName("Конструктор копирования создает точную копию списка")
        void copyConstructorCreatesExactCopy() {
            CustomCarList copy = new CustomCarList(listWithCars);
            assertEquals(listWithCars.size(), copy.size());
            assertEquals(listWithCars.get(0), copy.get(0));
            assertEquals(listWithCars.get(1), copy.get(1));
            assertEquals(listWithCars.get(2), copy.get(2));
        }
    }

    @Nested
    @DisplayName("Тесты добавления элементов")
    class AddTests {

        @Test
        @DisplayName("add() добавляет элемент в конец списка")
        void addAddsElementToEnd() {
            emptyList.add(car1);
            assertEquals(1, emptyList.size());
            assertEquals(car1, emptyList.get(0));

            emptyList.add(car2);
            assertEquals(2, emptyList.size());
            assertEquals(car2, emptyList.get(1));
        }

        @Test
        @DisplayName("add(null) выбрасывает исключение")
        void addNullThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> emptyList.add(null));
        }

        @Test
        @DisplayName("add(index) добавляет элемент на указанную позицию")
        void addAtIndexAddsElementAtPosition() {
            listWithCars.add(1, car4);
            assertEquals(4, listWithCars.size());
            assertEquals(car1, listWithCars.get(0));
            assertEquals(car4, listWithCars.get(1));
            assertEquals(car2, listWithCars.get(2));
            assertEquals(car3, listWithCars.get(3));
        }

        @Test
        @DisplayName("add(index) с неверным индексом выбрасывает исключение")
        void addAtIndexWithInvalidIndexThrowsException() {
            assertThrows(IndexOutOfBoundsException.class,
                    () -> listWithCars.add(-1, car4));
            assertThrows(IndexOutOfBoundsException.class,
                    () -> listWithCars.add(4, car4));
        }
    }

    @Nested
    @DisplayName("Тесты получения и изменения элементов")
    class GetAndSetTests {

        @Test
        @DisplayName("get() возвращает правильный элемент")
        void getReturnsCorrectElement() {
            assertEquals(car1, listWithCars.get(0));
            assertEquals(car2, listWithCars.get(1));
            assertEquals(car3, listWithCars.get(2));
        }

        @Test
        @DisplayName("get() с неверным индексом выбрасывает исключение")
        void getWithInvalidIndexThrowsException() {
            assertThrows(IndexOutOfBoundsException.class, () -> emptyList.get(0));
            assertThrows(IndexOutOfBoundsException.class, () -> listWithCars.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> listWithCars.get(3));
        }

        @Test
        @DisplayName("set() заменяет элемент и возвращает старый")
        void setReplacesElementAndReturnsOld() {
            Car oldCar = listWithCars.set(1, car4);
            assertEquals(car2, oldCar);
            assertEquals(car4, listWithCars.get(1));
            assertEquals(3, listWithCars.size());
        }
    }

    @Nested
    @DisplayName("Тесты удаления элементов")
    class RemoveTests {

        @Test
        @DisplayName("remove(index) удаляет элемент по индексу")
        void removeByIndexRemovesElement() {
            Car removed = listWithCars.remove(1);
            assertEquals(car2, removed);
            assertEquals(2, listWithCars.size());
            assertEquals(car1, listWithCars.get(0));
            assertEquals(car3, listWithCars.get(1));
        }

        @Test
        @DisplayName("remove(car) удаляет элемент по значению")
        void removeByValueRemovesElement() {
            boolean removed = listWithCars.remove(car2);
            assertTrue(removed);
            assertEquals(2, listWithCars.size());
            assertFalse(listWithCars.contains(car2));
        }

        @Test
        @DisplayName("remove(car) возвращает false для несуществующего элемента")
        void removeByValueReturnsFalseForNonExistent() {
            boolean removed = listWithCars.remove(car4);
            assertFalse(removed);
            assertEquals(3, listWithCars.size());
        }
    }

    @Nested
    @DisplayName("Тесты поиска элементов")
    class SearchTests {

        @Test
        @DisplayName("contains() возвращает true для существующего элемента")
        void containsReturnsTrueForExisting() {
            assertTrue(listWithCars.contains(car1));
            assertTrue(listWithCars.contains(car2));
            assertTrue(listWithCars.contains(car3));
        }

        @Test
        @DisplayName("contains() возвращает false для несуществующего элемента")
        void containsReturnsFalseForNonExistent() {
            assertFalse(listWithCars.contains(car4));
        }

        @Test
        @DisplayName("indexOf() возвращает правильный индекс")
        void indexOfReturnsCorrectIndex() {
            assertEquals(0, listWithCars.indexOf(car1));
            assertEquals(1, listWithCars.indexOf(car2));
            assertEquals(2, listWithCars.indexOf(car3));
        }

        @Test
        @DisplayName("indexOf() возвращает -1 для несуществующего элемента")
        void indexOfReturnsMinusOneForNonExistent() {
            assertEquals(-1, listWithCars.indexOf(car4));
        }
    }

    @Nested
    @DisplayName("Тесты автоматического расширения")
    class ResizeTests {

        @Test
        @DisplayName("Список автоматически расширяется при добавлении элементов")
        void listAutomaticallyResizes() {
            CustomCarList list = new CustomCarList(2);
            assertEquals(0, list.size());

            list.add(car1);
            list.add(car2);
            assertEquals(2, list.size());

            // Это должно вызвать расширение
            list.add(car3);
            assertEquals(3, list.size());
            assertEquals(car1, list.get(0));
            assertEquals(car2, list.get(1));
            assertEquals(car3, list.get(2));

            // Добавим еще для проверки
            list.add(car4);
            assertEquals(4, list.size());
        }
    }

    @Nested
    @DisplayName("Тесты сортировки")
    class SortTests {

        @Test
        @DisplayName("sort() сортирует по естественному порядку (бренд, модель, год)")
        void sortUsesNaturalOrder() {
            CustomCarList unsorted = new CustomCarList();
            unsorted.add(car2); // BMW
            unsorted.add(car1); // Toyota Camry 2020
            unsorted.add(car4); // Toyota Camry 2022
            unsorted.add(car3); // Honda

            unsorted.sort();

            // Ожидаемый порядок: BMW, Honda, Toyota Camry 2020, Toyota Camry 2022
            assertEquals(car2, unsorted.get(0)); // BMW
            assertEquals(car3, unsorted.get(1)); // Honda
            assertEquals(car1, unsorted.get(2)); // Toyota 2020
            assertEquals(car4, unsorted.get(3)); // Toyota 2022
        }

        @Test
        @DisplayName("sort(comparator) сортирует по компаратору")
        void sortWithComparator() {
            CustomCarList unsorted = new CustomCarList();
            unsorted.add(car2); // 60000
            unsorted.add(car1); // 25000
            unsorted.add(car3); // 22000
            unsorted.add(car4); // 27000

            // Сортировка по цене
            unsorted.sort(Comparator.comparingInt(Car::getPrice));

            assertEquals(car3, unsorted.get(0)); // 22000
            assertEquals(car1, unsorted.get(1)); // 25000
            assertEquals(car4, unsorted.get(2)); // 27000
            assertEquals(car2, unsorted.get(3)); // 60000
        }
    }

    @Nested
    @DisplayName("Тесты Stream API")
    class StreamTests {

        @Test
        @DisplayName("stream() создает поток элементов")
        void streamCreatesStream() {
            Stream<Car> stream = listWithCars.stream();
            assertNotNull(stream);
            assertEquals(3, stream.count());
        }

        @Test
        @DisplayName("Можно использовать Stream API для фильтрации")
        void canUseStreamForFiltering() {
            // Найти все Toyota
            long toyotaCount = listWithCars.stream()
                    .filter(car -> "Toyota".equals(car.getBrand()))
                    .count();
            assertEquals(1, toyotaCount);

            // Найти все автомобили дороже 30000
            long expensiveCount = listWithCars.stream()
                    .filter(car -> car.getPrice() > 30000)
                    .count();
            assertEquals(1, expensiveCount); // только BMW
        }

        @Test
        @DisplayName("Можно использовать Stream для создания новой коллекции")
        void canUseStreamToCreateNewCollection() {
            CustomCarList toyotas = new CustomCarList();
            listWithCars.stream()
                    .filter(car -> "Toyota".equals(car.getBrand()))
                    .forEach(toyotas::add);

            assertEquals(1, toyotas.size());
            assertEquals(car1, toyotas.get(0));
        }
    }

    @Nested
    @DisplayName("Тесты итератора")
    class IteratorTests {

        @Test
        @DisplayName("Итератор позволяет обойти все элементы")
        void iteratorIteratesOverAllElements() {
            Iterator<Car> iterator = listWithCars.iterator();
            assertTrue(iterator.hasNext());
            assertEquals(car1, iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(car2, iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(car3, iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("Итератор поддерживает удаление элементов")
        void iteratorSupportsRemove() {
            Iterator<Car> iterator = listWithCars.iterator();
            iterator.next(); // car1
            iterator.remove();

            assertEquals(2, listWithCars.size());
            assertEquals(car2, listWithCars.get(0));
            assertEquals(car3, listWithCars.get(1));
        }

        @Test
        @DisplayName("forEach работает с кастомной коллекцией")
        void forEachWorks() {
            StringBuilder result = new StringBuilder();
            listWithCars.forEach(car -> result.append(car.getBrand()).append(" "));

            assertEquals("Toyota BMW Honda ", result.toString());
        }
    }

    @Nested
    @DisplayName("Тесты добавления нескольких элементов")
    class AddAllTests {

        @Test
        @DisplayName("addAll добавляет все элементы из другой коллекции")
        void addAllAddsAllElements() {
            CustomCarList other = new CustomCarList();
            other.add(car4);

            listWithCars.addAll(other);

            assertEquals(4, listWithCars.size());
            assertEquals(car4, listWithCars.get(3));
        }

        @Test
        @DisplayName("addAll добавляет все элементы из массива")
        void addAllFromArray() {
            Car[] cars = {car4, car1};

            emptyList.addAll(cars);

            assertEquals(2, emptyList.size());
            assertEquals(car4, emptyList.get(0));
            assertEquals(car1, emptyList.get(1));
        }
    }

    @Nested
    @DisplayName("Тесты функциональных методов")
    class FunctionalTests {

        @Test
        @DisplayName("filter создает новый список с отфильтрованными элементами")
        void filterCreatesFilteredList() {
            CustomCarList toyotas = listWithCars.filter(car -> "Toyota".equals(car.getBrand()));

            assertEquals(1, toyotas.size());
            assertEquals(car1, toyotas.get(0));

            // Исходный список не изменился
            assertEquals(3, listWithCars.size());
        }

        @Test
        @DisplayName("toArray преобразует список в массив")
        void toArrayConvertsToArray() {
            Car[] array = listWithCars.toArray();

            assertEquals(3, array.length);
            assertEquals(car1, array[0]);
            assertEquals(car2, array[1]);
            assertEquals(car3, array[2]);
        }
    }
}
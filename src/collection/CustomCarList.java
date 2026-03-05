package collection;

import model.Car;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Objects;

/**
 * Кастомная реализация динамического списка для объектов Car
 * Поддерживает автоматическое расширение, итератор и Stream API
 * Полностью совместима с классом Car, предоставленным в задании
 */
public class CustomCarList implements Iterable<Car> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final float GROWTH_FACTOR = 1.5f;

    private Car[] elements;
    private int size;
    private int modCount; // Счетчик изменений для итератора

    // ========== Конструкторы ==========

    /**
     * Создает пустой список с начальной емкостью по умолчанию (10)
     */
    public CustomCarList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Создает пустой список с указанной начальной емкостью
     * @param initialCapacity начальная емкость списка
     * @throws IllegalArgumentException если initialCapacity < 0
     */
    public CustomCarList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative: " + initialCapacity);
        }
        this.elements = new Car[initialCapacity];
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * Создает список, содержащий элементы указанной коллекции
     * @param cars коллекция для копирования
     */
    public CustomCarList(CustomCarList cars) {
        this.elements = new Car[cars.size()];
        System.arraycopy(cars.elements, 0, this.elements, 0, cars.size());
        this.size = cars.size();
        this.modCount = 0;
    }

    // ========== Основные методы работы со списком ==========

    /**
     * Добавляет автомобиль в конец списка
     * @param car автомобиль для добавления (не может быть null)
     * @throws IllegalArgumentException если car == null
     */
    public void add(Car car) {
        Objects.requireNonNull(car, "Car cannot be null");
        ensureCapacity();
        elements[size++] = car;
        modCount++;
    }

    /**
     * Добавляет автомобиль на указанную позицию
     * @param index позиция для вставки
     * @param car автомобиль для добавления
     * @throws IndexOutOfBoundsException если index < 0 или index > size
     * @throws IllegalArgumentException если car == null
     */
    public void add(int index, Car car) {
        Objects.requireNonNull(car, "Car cannot be null");
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        // Сдвигаем элементы вправо
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = car;
        size++;
        modCount++;
    }

    /**
     * Возвращает автомобиль по индексу
     * @param index индекс элемента
     * @return автомобиль на указанной позиции
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    public Car get(int index) {
        checkIndex(index);
        return elements[index];
    }

    /**
     * Заменяет автомобиль на указанной позиции
     * @param index индекс заменяемого элемента
     * @param car новый автомобиль
     * @return предыдущий автомобиль на этой позиции
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     * @throws IllegalArgumentException если car == null
     */
    public Car set(int index, Car car) {
        Objects.requireNonNull(car, "Car cannot be null");
        checkIndex(index);
        Car oldCar = elements[index];
        elements[index] = car;
        modCount++;
        return oldCar;
    }

    /**
     * Удаляет автомобиль по индексу
     * @param index индекс удаляемого элемента
     * @return удаленный автомобиль
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    public Car remove(int index) {
        checkIndex(index);
        Car removed = elements[index];

        // Сдвигаем элементы влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Помогаем сборщику мусора
        modCount++;

        return removed;
    }

    /**
     * Удаляет первый найденный указанный автомобиль
     * @param car автомобиль для удаления
     * @return true если автомобиль был найден и удален
     */
    public boolean remove(Car car) {
        if (car == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (car.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает текущий размер списка
     * @return количество элементов в списке
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуст ли список
     * @return true если список не содержит элементов
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает список (удаляет все элементы)
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
        modCount++;
    }

    /**
     * Проверяет, содержится ли автомобиль в списке
     * @param car искомый автомобиль
     * @return true если автомобиль найден
     */
    public boolean contains(Car car) {
        return indexOf(car) != -1;
    }

    /**
     * Возвращает индекс первого вхождения автомобиля
     * @param car искомый автомобиль
     * @return индекс или -1 если не найден
     */
    public int indexOf(Car car) {
        if (car == null) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (car.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Возвращает индекс последнего вхождения автомобиля
     * @param car искомый автомобиль
     * @return индекс или -1 если не найден
     */
    public int lastIndexOf(Car car) {
        if (car == null) {
            return -1;
        }
        for (int i = size - 1; i >= 0; i--) {
            if (car.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    // ========== Методы для работы с несколькими элементами ==========

    /**
     * Добавляет все элементы из другой коллекции
     * @param other коллекция для добавления
     */
    public void addAll(CustomCarList other) {
        if (other == null || other.isEmpty()) {
            return;
        }
        ensureCapacity(size + other.size());
        for (Car car : other) {
            add(car);
        }
    }

    /**
     * Добавляет все элементы из массива
     * @param cars массив автомобилей
     */
    public void addAll(Car[] cars) {
        if (cars == null || cars.length == 0) {
            return;
        }
        ensureCapacity(size + cars.length);
        for (Car car : cars) {
            add(car);
        }
    }

    /**
     * Преобразует список в массив
     * @return массив, содержащий все элементы списка
     */
    public Car[] toArray() {
        Car[] result = new Car[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    // ========== Методы сортировки ==========

    /**
     * Сортирует список с использованием естественного порядка (compareTo)
     */
    public void sort() {
        sort(null);
    }

    /**
     * Сортирует список с использованием указанного компаратора
     * @param comparator компаратор для сортировки
     */
    public void sort(Comparator<? super Car> comparator) {
        if (size > 1) {
            // Простая сортировка пузырьком для демонстрации
            // В реальном проекте лучше использовать Arrays.sort()
            bubbleSort(comparator);
        }
        modCount++;
    }

    private void bubbleSort(Comparator<? super Car> comparator) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                boolean shouldSwap;
                if (comparator == null) {
                    // Используем compareTo из Car
                    shouldSwap = elements[j].compareTo(elements[j + 1]) > 0;
                } else {
                    // Используем переданный компаратор
                    shouldSwap = comparator.compare(elements[j], elements[j + 1]) > 0;
                }

                if (shouldSwap) {
                    // Меняем местами
                    Car temp = elements[j];
                    elements[j] = elements[j + 1];
                    elements[j + 1] = temp;
                }
            }
        }
    }

    // ========== Stream API поддержка ==========

    /**
     * Создает последовательный Stream из элементов списка
     * @return Stream элементов списка
     */
    public Stream<Car> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Создает параллельный Stream из элементов списка
     * @return параллельный Stream элементов списка
     */
    public Stream<Car> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    // ========== Итератор ==========

    @Override
    public Iterator<Car> iterator() {
        return new Iterator<Car>() {
            private int currentIndex = 0;
            private int expectedModCount = modCount;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public Car next() {
                checkForComodification();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                canRemove = true;
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException("remove() can only be called once after next()");
                }
                checkForComodification();
                CustomCarList.this.remove(--currentIndex);
                expectedModCount = modCount;
                canRemove = false;
            }

            private void checkForComodification() {
                if (modCount != expectedModCount) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        };
    }

    @Override
    public Spliterator<Car> spliterator() {
        return Spliterators.spliterator(elements, 0, size,
                Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.SIZED);
    }

    // ========== Функциональные методы ==========

    /**
     * Выполняет указанное действие для каждого элемента списка
     * @param action действие для выполнения
     */
    public void forEach(Consumer<? super Car> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < size; i++) {
            action.accept(elements[i]);
        }
    }

    /**
     * Фильтрует список, оставляя только элементы, удовлетворяющие условию
     * @param predicate условие фильтрации
     * @return новый список с отфильтрованными элементами
     */
    public CustomCarList filter(java.util.function.Predicate<? super Car> predicate) {
        Objects.requireNonNull(predicate);
        CustomCarList result = new CustomCarList();
        for (int i = 0; i < size; i++) {
            if (predicate.test(elements[i])) {
                result.add(elements[i]);
            }
        }
        return result;
    }

    // ========== Вспомогательные методы ==========

    /**
     * Обеспечивает достаточную вместимость массива
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            resize();
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            // Вычисляем новый размер
            int newCapacity = elements.length;
            while (newCapacity < minCapacity) {
                newCapacity = (int)(newCapacity * GROWTH_FACTOR);
            }
            resize(newCapacity);
        }
    }

    /**
     * Увеличивает размер массива в GROWTH_FACTOR раз
     */
    private void resize() {
        int newCapacity = (int)(elements.length * GROWTH_FACTOR);
        // Убедимся, что мы хотя бы на 1 увеличили
        if (newCapacity == elements.length) {
            newCapacity = elements.length + 1;
        }
        resize(newCapacity);
    }

    private void resize(int newCapacity) {
        Car[] newArray = new Car[newCapacity];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    /**
     * Проверяет корректность индекса
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    String.format("Index: %d, Size: %d", index, size)
            );
        }
    }

    // ========== Переопределенные методы Object ==========

    @Override
    public String toString() {
        if (size == 0) {
            return "CustomCarList[]";
        }

        StringBuilder sb = new StringBuilder("CustomCarList[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i].toString());
            if (i < size - 1) {
                sb.append(",\n ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomCarList that = (CustomCarList) o;
        if (size != that.size) return false;

        for (int i = 0; i < size; i++) {
            if (!Objects.equals(elements[i], that.elements[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + (elements[i] == null ? 0 : elements[i].hashCode());
        }
        return result;
    }
}
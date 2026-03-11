package com.example.util;

import java.util.ArrayList;
import java.util.List;

public class InputValidator {
    
    // Валидация целого числа в диапазоне
    public static ValidationResult<Integer> validateInt(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value < min) {
                return ValidationResult.error("Значение не может быть меньше " + min);
            }
            if (value > max) {
                return ValidationResult.error("Значение не может быть больше " + max);
            }
            return ValidationResult.success(value);
        } catch (NumberFormatException e) {
            return ValidationResult.error("Введите корректное целое число");
        }
    }

    // Валидация строки (не пустая, не длиннее maxLength)
    public static ValidationResult<String> validateString(String input, int maxLength, boolean allowEmpty) {
        String trimmed = input.trim();
        
        if (!allowEmpty && trimmed.isEmpty()) {
            return ValidationResult.error("Строка не может быть пустой");
        }
        
        if (trimmed.length() > maxLength) {
            return ValidationResult.error("Строка не может быть длиннее " + maxLength + " символов");
        }
        
        // Проверка на недопустимые символы (только буквы, цифры, пробелы и дефисы)
        if (!trimmed.matches("[а-яА-Яa-zA-Z0-9\\s-]*")) {
            return ValidationResult.error("Строка содержит недопустимые символы. Разрешены: буквы, цифры, пробелы, дефисы");
        }
        
        return ValidationResult.success(trimmed);
    }

    // Валидация года (для автомобиля)
    public static ValidationResult<Integer> validateYear(String input) {
        return validateInt(input, 1900, 2026);
    }

    // Валидация мощности (для автомобиля)
    public static ValidationResult<Integer> validatePower(String input) {
        return validateInt(input, 50, 2000);
    }

    // Валидация строки из файла (разбивка по разделителю)
    public static ValidationResult<String[]> validateFileLine(String line, String delimiter, int expectedParts) {
        if (line == null || line.trim().isEmpty()) {
            return ValidationResult.error("Пустая строка");
        }
        
        String[] parts = line.split(delimiter);
        if (parts.length != expectedParts) {
            return ValidationResult.error("Ожидалось " + expectedParts + " полей, получено " + parts.length);
        }
        
        return ValidationResult.success(parts);
    }

    // Класс-обертка для результата валидации
    public static class ValidationResult<T> {
        private final boolean valid;
        private final T value;
        private final String errorMessage;

        private ValidationResult(boolean valid, T value, String errorMessage) {
            this.valid = valid;
            this.value = value;
            this.errorMessage = errorMessage;
        }

        public static <T> ValidationResult<T> success(T value) {
            return new ValidationResult<>(true, value, null);
        }

        public static <T> ValidationResult<T> error(String message) {
            return new ValidationResult<>(false, null, message);
        }

        public boolean isValid() { return valid; }
        public T getValue() { return value; }
        public String getErrorMessage() { return errorMessage; }
    }
}
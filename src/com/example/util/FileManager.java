package com.example.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    // Чтение всех строк из файла
    public static FileReadResult readLines(String filename) {
        List<String> lines = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        Path filePath = Paths.get(filename);
        
        // Проверка существования файла
        if (!Files.exists(filePath)) {
            errors.add("Файл не найден: " + filename);
            return new FileReadResult(lines, errors);
        }
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                // Пропускаем пустые строки и комментарии
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                lines.add(line);
            }
            
        } catch (IOException e) {
            errors.add("Ошибка чтения файла: " + e.getMessage());
        }
        
        return new FileReadResult(lines, errors);
    }

    // Запись строки в файл (append = true - добавление в конец)
    public static FileWriteResult writeLine(String filename, String line, boolean append) {
        List<String> errors = new ArrayList<>();
        
        try {
            Path filePath = Paths.get(filename);
            
            // Создаем директории, если их нет
            Path parentDir = filePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Запись с BufferedWriter для производительности
            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath, 
                    append ? StandardOpenOption.APPEND : StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
                )) {
                
                // Если файл новый или не append, добавляем заголовок
                if (!append || Files.size(filePath) == 0) {
                    writer.write("# Формат: мощность,модель,год");
                    writer.newLine();
                }
                
                writer.write(line);
                writer.newLine();
                
                return new FileWriteResult(true, errors);
            }
            
        } catch (IOException e) {
            errors.add("Ошибка записи в файл: " + e.getMessage());
            return new FileWriteResult(false, errors);
        }
    }

    // Запись нескольких строк
    public static FileWriteResult writeAllLines(String filename, List<String> lines, boolean append) {
        FileWriteResult result = null;
        
        for (String line : lines) {
            result = writeLine(filename, line, append);
            if (!result.isSuccess()) {
                return result; // Возвращаем первую ошибку
            }
            // После первой записи включаем append
            append = true;
        }
        
        return result != null ? result : new FileWriteResult(true, new ArrayList<>());
    }

    // Проверка существования файла
    public static boolean fileExists(String filename) {
        return Files.exists(Paths.get(filename));
    }

    // Получение размера файла
    public static long getFileSize(String filename) {
        try {
            return Files.size(Paths.get(filename));
        } catch (IOException e) {
            return -1;
        }
    }

    // Класс для результата чтения
    public static class FileReadResult {
        private final List<String> lines;
        private final List<String> errors;

        public FileReadResult(List<String> lines, List<String> errors) {
            this.lines = lines;
            this.errors = errors;
        }

        public List<String> getLines() { return lines; }
        public List<String> getErrors() { return errors; }
        public boolean hasErrors() { return !errors.isEmpty(); }
        public boolean isEmpty() { return lines.isEmpty(); }
    }

    // Класс для результата записи
    public static class FileWriteResult {
        private final boolean success;
        private final List<String> errors;

        public FileWriteResult(boolean success, List<String> errors) {
            this.success = success;
            this.errors = errors;
        }

        public boolean isSuccess() { return success; }
        public List<String> getErrors() { return errors; }
        public boolean hasErrors() { return !errors.isEmpty(); }
    }
}
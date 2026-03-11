package service;

import collection.CustomCarList;
import model.Car;

public class MultiThreadCounter implements CounterService {

    private final int threadCount;

    public MultiThreadCounter(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be > 0");
        }
        this.threadCount = threadCount;
    }

    @Override
    public int countCarsByYear(CustomCarList list, int year) {

        int size = list.size();

        if (size == 0) {
            return 0;
        }

        int partSize = size / threadCount;

        Thread[] threads = new Thread[threadCount];
        CounterTask[] tasks = new CounterTask[threadCount];

        int start = 0;

        for (int i = 0; i < threadCount; i++) {

            int end = (i == threadCount - 1) ? size : start + partSize;

            tasks[i] = new CounterTask(list, year, start, end);
            threads[i] = new Thread(tasks[i]);

            threads[i].start();

            start = end;
        }

        int result = 0;

        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
                result += tasks[i].getCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static class CounterTask implements Runnable {

        private final CustomCarList list;
        private final int year;
        private final int start;
        private final int end;

        private int count;

        public CounterTask(CustomCarList list, int year, int start, int end) {
            this.list = list;
            this.year = year;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {

            int localCount = 0;

            for (int i = start; i < end; i++) {

                Car car = list.get(i);

                if (car.getYear() == year) {
                    localCount++;
                }
            }

            count = localCount;
        }

        public int getCount() {
            return count;
        }
    }
}
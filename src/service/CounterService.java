package service;

import collection.CustomCarList;
import model.Car;

public interface CounterService {

    int countCarsByYear(CustomCarList list, int year);

}
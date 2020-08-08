package iwona.pl.modol10test.service;

import iwona.pl.modol10test.model.Car;

import iwona.pl.modol10test.model.Color;
import java.util.List;
import java.util.Optional;


public interface CarServiceInter {

    List<Car> getAll();

    Optional<Car> carById(Long carId);

    List<Car> carByColor(String color);

    boolean save(Car car);

    Optional<Car> changeCar(Long carId, Car changedCar);

    boolean changeColor(Long carId, Color color);

    boolean changeMark(Long id, String newMark);

    boolean removeById(Long carId);
}

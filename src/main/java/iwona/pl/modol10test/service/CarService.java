package iwona.pl.modol10test.service;


import iwona.pl.modol10test.exception.CarNotExist;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.model.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CarService implements CarServiceInter {

    private final List<Car> cars;

    public CarService() {
        this.cars = new ArrayList<>();
        cars.add(new Car(1L, "Ferrari", "599 GTB Fiorano", Color.RED));
        cars.add(new Car(2L, "Audi", "A6", Color.NAVY_BLUE));
        cars.add(new Car(3L, "Aston Martin", "DB5", Color.RED));
    }

    @Override
    public List<Car> getAll() {
        return cars;
    }

    @Override
    public Optional<Car> carById(Long carId) {
        Car findCarById = cars.stream()
                .filter(car -> car.getCarId().equals(carId))
                .findFirst()
                .orElseThrow(() -> new CarNotExist(carId));
        return Optional.of(findCarById);
    }

    @Override
    public List<Car> carByColor(String color) {
        return getAll().stream().filter(car -> color.equalsIgnoreCase(car.getColor().name()))
                .collect(Collectors.toList());
    }

    @Override //post
    public boolean save(Car car) {
        return cars.add(car);
    }

    @Override //put
    public Optional<Car> changeCar(Long carId, Car changedCar) {
        Car car = cars.stream()
                .filter(c -> c.getCarId().equals(carId))
                .findFirst()
                .orElseThrow(() -> new CarNotExist(carId));
        car.setMark(changedCar.getMark());
        car.setModel(changedCar.getModel());
        car.setColor(changedCar.getColor());
        return Optional.of(car);
    }

    @Override
    public boolean changeColor(Long carId, Color color) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId().equals(carId)).findFirst();
        if (first.isPresent()) {
            Car carColor = first.get();
            carColor.setColor(color);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeMark(Long id, String newMark) {
        Optional<Car> findMark = cars.stream().filter(car -> car.getCarId().equals(id)).findFirst();
        if (findMark.isPresent()) {
            Car carModel = findMark.get();
            carModel.setMark(newMark);
            return true;
        }
        throw new CarNotExist(id);
    }

    @Override
    public boolean removeById(Long carId) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId().equals(carId)).findFirst();
        if (first.isPresent()) {
            cars.remove(first.get());
            return true;
        }
        throw new CarNotExist(carId);
    }
}

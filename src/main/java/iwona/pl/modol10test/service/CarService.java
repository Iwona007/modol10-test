package iwona.pl.modol10test.service;


import iwona.pl.modol10test.converColor.ConvertColor;
import iwona.pl.modol10test.exception.CarNotExist;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.model.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CarService implements CarServiceInter {

    private List<Car> cars;
    private ConvertColor convertColor;
    Car car1 = new Car(1L, "Ferrari", "599 GTB Fiorano", Color.RED);
    Car car2 = new Car(2L, "Audi", "A6", Color.NAVY_BLUE);
    Car car3 = new Car(3L, "Aston Martin", "DB5", Color.RED);

    @Autowired
    public CarService(ConvertColor convertColor) {
        this.convertColor = convertColor;
        this.cars = new ArrayList<>();
        cars.add(new Car(1L, "Ferrari", "599 GTB Fiorano", Color.RED));
        cars.add(new Car(2L, "Audi", "A6", Color.NAVY_BLUE));
        cars.add(new Car(3L, "Aston Martin", "DB5", Color.RED));
    }

    @Override
    public List<Car> getAll() {
        return cars;
    }

    @Override  //get
    public Optional<Car> carById(Long carId) {
        Optional<Car> findCarById = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        findCarById.orElseThrow(() -> new CarNotExist(carId));
        return findCarById;
    }

    @Override //get by color
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
        Optional<Car> findCar = Optional.ofNullable(cars.stream().filter(car -> car.getCarId() == changedCar.getCarId()).findFirst()
                .orElseThrow(() -> new CarNotExist(carId)));

        return findCar.map(car -> {
            car.setMark(changedCar.getMark());
            car.setModel(changedCar.getModel());
            car.setColor(changedCar.getColor());
            return car;
        });
    }

    @Override //    patch
    public boolean changeColor(Long carId, Color color) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        if (first.isPresent()) {
            Car carColor = first.get();
            carColor.setColor(color);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeMark(Long id, String newMark) {
        Optional<Car> findMark = cars.stream().filter(car -> car.getCarId() == id).findFirst();
        if (findMark.isPresent()) {
            Car carModel = findMark.get();
            carModel.setMark(newMark);
            return true;
        }
        throw new CarNotExist(id);
    }

    @Override //delete
    public boolean removeById(Long carId) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        if (first.isPresent()) {
            cars.remove(first.get());
            return true;
        }
        throw new CarNotExist(carId);
    }
}

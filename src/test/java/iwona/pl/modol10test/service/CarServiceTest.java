package iwona.pl.modol10test.service;

import iwona.pl.modol10test.exception.CarNotExist;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.model.Color;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarServiceTest {

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService();
    }

    @Test
    @DisplayName("Should get all cars test")
    void shouldGetAllCarsTest() {
        List<Car> cars = carService.getAll();

        assertEquals(3, cars.size());
        assertEquals("Ferrari", cars.get(0).getMark());
    }

    @Test
    @DisplayName("Should find car by id test")
    void shouldFindCarByIdTest() {
        Car car = carService.carById(2L).get();

        assertEquals(2L, car.getCarId());
        assertEquals("Audi", car.getMark());
    }

    @Test
    @DisplayName("Should throw CarNotExist when car with given id does not exist test")
    void shouldNotFindCarByIdTest() {
        assertThrows(CarNotExist.class, () -> carService.carById(10L));
    }

    @Test
    @DisplayName("Should find car by color test")
    void shouldFindCarByColor() {
        List<Car> cars = carService.carByColor("red");

        assertEquals(2, cars.size());
        cars.forEach(car -> assertEquals(Color.RED, car.getColor()));
    }

    @Test
    @DisplayName("Should return empty list when no car has given color test")
    void shouldNotFindCarByColor() {
        assertTrue(carService.carByColor("BLUE").isEmpty());
    }

    @Test
    @DisplayName("Should save car test")
    void shouldSaveCarTest() {
        Car car = new Car(4L, "Audi", "C5", Color.RED);

        assertTrue(carService.save(car));

        assertEquals(4, carService.getAll().size());
        assertEquals("Audi", carService.getAll().get(3).getMark());
    }

    @Test
    @DisplayName("Should change car found by id from path, not by id from body test")
    void shouldChangeCarTest() {
        Car changed = new Car(99L, "Toyota", "Yaris", Color.SILVER);

        Car result = carService.changeCar(2L, changed).get();

        assertEquals(2L, result.getCarId());
        assertEquals("Toyota", result.getMark());
        assertEquals("Yaris", result.getModel());
        assertEquals(Color.SILVER, result.getColor());
        assertEquals("Toyota", carService.carById(2L).get().getMark());
    }

    @Test
    @DisplayName("Should throw CarNotExist when changing car that does not exist test")
    void shouldNotChangeCarWhenCarNotExistTest() {
        Car changed = new Car(10L, "Toyota", "Yaris", Color.SILVER);

        assertThrows(CarNotExist.class, () -> carService.changeCar(10L, changed));
    }

    @Test
    @DisplayName("Should change color of existing car and report false for missing one test")
    void shouldChangeColorTest() {
        assertTrue(carService.changeColor(1L, Color.BLUE));
        assertEquals(Color.BLUE, carService.carById(1L).get().getColor());
        assertFalse(carService.changeColor(10L, Color.BLUE));
    }

    @Test
    @DisplayName("Should change mark of existing car and throw for missing one test")
    void shouldChangeMarkTest() {
        assertTrue(carService.changeMark(1L, "BMW"));
        assertEquals("BMW", carService.carById(1L).get().getMark());
        assertThrows(CarNotExist.class, () -> carService.changeMark(10L, "BMW"));
    }

    @Test
    @DisplayName("Should remove car by id test")
    void shouldRemoveCarByIdTest() {
        assertTrue(carService.removeById(3L));

        assertEquals(2, carService.getAll().size());
        assertThrows(CarNotExist.class, () -> carService.removeById(3L));
    }
}

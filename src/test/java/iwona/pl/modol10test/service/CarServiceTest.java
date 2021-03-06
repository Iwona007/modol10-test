package iwona.pl.modol10test.service;

import iwona.pl.modol10test.exception.CarNotExist;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.model.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CarServiceTest {

    @Spy
    private CarServiceInter carServiceInter;

    @BeforeEach
    public void find() {
        given(carServiceInter.getAll()).willReturn(prepareMockData());
    }

    @AfterEach
    public void cleanUp() {
        carServiceInter.getAll().clear();
    }

    private List<Car> prepareMockData() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car(1L, "Marka1", "Model1", Color.RED));
        cars.add(new Car(2L, "Marka2", "Model2", Color.BLUE));
        cars.add(new Car(3L, "Marka3", "Model3", Color.SILVER));
        return cars;
    }

    @Test
    @DisplayName("Should get all cars test")
    void shouldGetAllCarsTest() {
//        given:
        List<Car> carsList = prepareMockData();
        given(carServiceInter.getAll()).willReturn(carsList);
//        when:
        List<Car> car = carServiceInter.getAll();
//       then:
        assertThat(car, Matchers.hasSize(3));
    }

    @Test
    @DisplayName("Should find car by id test")
    void shouldFindCarByIdTest() {
//        given
        Car car = new Car(2L, "Marka1", "Model1", Color.RED);
        given(carServiceInter.carById(2L)).willReturn(Optional.of(car));
//        when
        Long carId = carServiceInter.carById(2L).get().getCarId();
//        then
        assertEquals(2L, carId);
        assertNotEquals(1L, carId);
    }

    @Test
    @DisplayName("Should find car by color test")
    void shouldFindCarByColor() {
//        given:
        given(carServiceInter.carByColor("RED")).willReturn(prepareMockData().stream()
                .filter(car -> car.getColor().equals(Color.valueOf("RED"))).collect(Collectors.toList()));
//        when:
        String color = carServiceInter.carByColor("RED").get(0).getColor().name();
//        then:
        assertEquals("RED", color);
    }

    @Test
    @DisplayName("Should save car test")
    void shouldSaveCarTest() {
//        given
        Car car = new Car(4L, "Audi", "C5", Color.RED);
//        when
        carServiceInter.getAll().add(car);
//        then
        assertEquals(4L, carServiceInter.getAll().size());
        assertThat(carServiceInter.getAll(), Matchers.hasSize(4));
        assertEquals("Audi", carServiceInter.getAll().get(3).getMark());
    }

    @Test
    @DisplayName("Should change car test")
    void shouldChangeCarTest() {
//        given
        Car newCar = new Car(2L, "Audi", "C5", Color.SILVER);
//        when
        Optional<Car> findCar = prepareMockData().stream().filter(car -> car.getCarId().equals(newCar.getCarId())).findFirst();
       findCar.map(car -> {
            car.setMark(newCar.getMark());
            car.setModel(newCar.getModel());
            car.setColor(newCar.getColor());
            return car;
        });

        carServiceInter.changeCar(findCar.get().getCarId(), newCar);
//        then
        Assert.assertEquals(newCar.getCarId(), findCar.get().getCarId());
        Assert.assertEquals(newCar.getModel(), findCar.get().getModel());
        Assert.assertEquals(newCar.getMark(), findCar.get().getMark());
        Assert.assertEquals(newCar.getColor(), findCar.get().getColor());
    }
}

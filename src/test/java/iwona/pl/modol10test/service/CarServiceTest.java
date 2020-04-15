package iwona.pl.modol10test.service;

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
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    void should_get_All_cars() {
//        given:
        List<Car> carsList = prepareMockData();
        given(carServiceInter.getAll()).willReturn(carsList);
//        when:
        List<Car> car = carServiceInter.getAll();
        //        then:
        assertThat(car, Matchers.hasSize(3));
    }

    @Test
    void carById() {
//        given
        Car car = new Car(1L, "Marka1", "Model1", Color.RED);
        given(carServiceInter.carById(1L)).willReturn(Optional.of(car));
//        when
        Long carId = carServiceInter.carById(1L).get().getCarId();
//        then
        assertEquals(1L, carId);
        assertNotEquals(2L, carId);
    }

    @Test
    void carByColor() {
//        given:
        given(carServiceInter.carByColor("RED")).willReturn(prepareMockData().stream()
                .filter(car -> car.getColor().equals(Color.valueOf("RED"))).collect(Collectors.toList()));
//        when:
        String color = carServiceInter.carByColor("RED").get(0).getColor().name();
//        then:
        assertEquals("RED", color);
    }

    @Test
    void save() {
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
    void changeCar() {
//        given
        Car newCar = new Car(2L, "Audi", "C5", Color.SILVER);
//when
        Optional<Car> findCar = prepareMockData().stream().filter(car -> car.getCarId() == 2l).findFirst();
        if (findCar.isPresent()) {
            Car car = findCar.get();
            car.setMark(newCar.getMark());
            car.setModel(newCar.getModel());
            car.setColor(newCar.getColor());
        }

        carServiceInter.changeCar(findCar.get().getCarId(), newCar);
//        then
        Assert.assertEquals(newCar.getCarId(), findCar.get().getCarId());
        Assert.assertEquals(newCar.getModel(), findCar.get().getModel());
        Assert.assertEquals(newCar.getMark(), findCar.get().getMark());
        Assert.assertEquals(newCar.getColor(), findCar.get().getColor());
    }
}

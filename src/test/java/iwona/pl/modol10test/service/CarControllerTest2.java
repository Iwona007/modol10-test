package iwona.pl.modol10test.service;

import iwona.pl.modol10test.controller.CarApi;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.model.Color;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CarControllerTest2 {

    @Mock
    private CarServiceInter carServiceInter;

    @InjectMocks
    private CarApi carApi;

    @BeforeEach
    public void init() {
        List<Car> carsList = prepareMockData();
        given(carServiceInter.getAll()).willReturn(carsList);
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
        List<Car> car = carApi.getAllCars();
        assertThat(car, Matchers.hasSize(3));
    }

    @Test
    void carById() {
    }

    @Test
    void carByColor() {
    }

    @Test
    void save() {
    }

    @Test
    void changeCar() {
    }

    @Test
    void changeColor() {
    }

    @Test
    void changeMark() {
    }

    @Test
    void removeById() {
    }
}

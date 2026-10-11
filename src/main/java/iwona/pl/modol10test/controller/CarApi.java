package iwona.pl.modol10test.controller;


import iwona.pl.modol10test.converColor.ConvertColor;
import iwona.pl.modol10test.exception.CarNotExist;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.service.CarServiceInter;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarApi {

    private final CarServiceInter carServiceInter;
    private final ConvertColor convertColor;

    public CarApi(CarServiceInter carServiceInter, ConvertColor convertColor) {
        this.carServiceInter = carServiceInter;
        this.convertColor = convertColor;
    }

    @GetMapping
    public List<Car> getAllCars() {  //Liste zamieniam na Resources
        return carServiceInter.getAll();
    }

    @GetMapping("/{carId}")
    public ResponseEntity<Car> getById(@PathVariable @NotNull Long carId) {   //
        Optional<Car> findCar = carServiceInter.carById(carId);
        if (findCar.isPresent()) {
            return new ResponseEntity<>(findCar.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<Car> getByColor(@PathVariable @NotNull String color) { // zamieniam liste na na Resources
        List<Car> findColor = carServiceInter.carByColor(color);
        if (!findColor.isEmpty()) {
            return new ResponseEntity<>( HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new")
    public ResponseEntity<Car> addCar(@Valid @RequestBody Car car) {
        if (carServiceInter.save(car)) {
            return new ResponseEntity<>(car, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Car> modifyCar(@PathVariable Long id, @Valid @RequestBody Car modifyCar) {
        Car car = carServiceInter.changeCar(id, modifyCar)
                .orElseThrow(() -> new CarNotExist(id));
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PatchMapping("/{carId}/color/{newColor}")
    public ResponseEntity<Car> updateColor(@PathVariable Long carId, @PathVariable @NotNull String newColor) {
        boolean changeColor = carServiceInter.changeColor(carId, convertColor.convertToEnum(newColor));
        if (changeColor) {
            return new ResponseEntity<>( HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}/mark/{newMark}")
    public ResponseEntity<Car> updateMark(@PathVariable Long id, @PathVariable @NotNull String newMark) {
        boolean changedMark = carServiceInter.changeMark(id, newMark);
        if (changedMark) {
            return new ResponseEntity<>( HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteById(@PathVariable @NotNull Long id) {
        boolean remove = carServiceInter.removeById(id);
        if (remove) {
            return new ResponseEntity(true, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

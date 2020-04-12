package iwona.pl.modol10test.controller;


import iwona.pl.modol10test.converColor.ConvertColor;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.service.CarServiceInter;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarApi {

    private CarServiceInter carServiceInter;
    private ConvertColor convertColor;

    @Autowired
    public CarApi(CarServiceInter carServiceInter, ConvertColor convertColor) {
        this.carServiceInter = carServiceInter;
        this.convertColor = convertColor;
    }

    @GetMapping
    public List<Car> getAllCars() {  //Liste zamieniam na Resources
        return carServiceInter.getAll();
    }

    @GetMapping("/{carId}")
    public Optional<Car> getById(@PathVariable @NotNull Long carId) {   //
        Optional<Car> findCar = carServiceInter.carById(carId);
        return findCar;
    }

    @GetMapping("/color/{color}")
    public List<Car> getByColor(@PathVariable @NotNull String color) { // zamieniam liste na na Resources
        List<Car> findColor = carServiceInter.carByColor(color);
        return findColor;
    }

    @PostMapping("/new")
    public ResponseEntity<Car> addCar(@Valid @RequestBody Car car) {
        if (carServiceInter.save(car)) {
            return new ResponseEntity(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @PutMapping("/modify/{id}")
//    public Car modifyCar(@PathVariable Long id, @Valid @RequestBody Car modifyCar) {
//
//        if (carServiceInter.changeCar(id, modifyCar)) {
//            return carServiceInter.changeCar(id, modifyCar);
//
//        } else if (!carServiceInter.changeCar(id, modifyCar)) {
//
//        }

    @PatchMapping("/{carId}/color/{newColor}")
    public ResponseEntity<Car> updateColor(@PathVariable Long carId, @PathVariable @NotNull String newColor) {
        boolean changeColor = carServiceInter.changeColor(carId, convertColor.convertToEnum(newColor));
        if (changeColor) {
            return new ResponseEntity(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PatchMapping("/{id}/mark/{newMark}")
    public ResponseEntity<Car> updateMark(@PathVariable Long id, @PathVariable String newMark) {
        boolean changedMark = carServiceInter.changeMark(id, newMark);
        if (changedMark) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteById(@PathVariable @NotNull Long id) {
        boolean remove = carServiceInter.removeById(id);
        if (remove) {
            return new ResponseEntity(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

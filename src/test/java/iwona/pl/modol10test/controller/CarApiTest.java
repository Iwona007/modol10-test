package iwona.pl.modol10test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iwona.pl.modol10test.model.Car;
import iwona.pl.modol10test.model.Color;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DirtiesContext
    void should_get_all_cars_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].mark").value("Ferrari"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].mark").value("Audi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].mark").value("Aston Martin"));
    }

    @Test
    @DirtiesContext
    void should_get_car_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/{carId}", 2L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("Audi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("A6"));
    }

    @Test
    @DirtiesContext
    void should_not_get_car_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/{carId}", 4L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(containsString("Car with given id: 4 not exist")));
    }

    @Test
    @DirtiesContext
    void should_get_by_color_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/color/{color}", Color.RED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DirtiesContext
    void should_not_get_By_Color_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/color/{color}", Color.BLUE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DirtiesContext
    void should_add_new_Car_test() throws Exception {
        Car car = new Car(5L, "Audi", "C5", Color.RED);
        String jsonRequest = objectMapper.writeValueAsString(car);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cars/new")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("Audi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("C5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("RED"));
//                .andReturn();
//        result.getResponse().getContentAsString();
    }

    @Test
    @DirtiesContext
    void should_not_add_new_Car_test() throws Exception {
//        Car car = new Car(null, "Audi", "", Color.RED);
//        String jsonRequest = objectMapper.writeValueAsString(car);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cars/new")
//                .content(jsonRequest)
                .content(asJsonString(new Car(null, "Audi", "", Color.RED)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        result.getResponse().getContentAsString();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @DirtiesContext
    void should_modify_car_with_exist_id_test() throws Exception {
//        given
        Car car = new Car(1L, "BMW", "S5", Color.RED);
//        then
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/modify/{id}", car.getCarId())
                .content(asJsonString(new Car(car.getCarId(), "Toyota", "Rav4", Color.SILVER)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value(car.getCarId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("Toyota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Rav4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("SILVER"))
                .andReturn();
        result.getResponse().getContentAsString();
    }

    @Test
    @DirtiesContext
    void should_not_modify_car_bad_request_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/modify/{id}", 2L)
                .content(asJsonString(new Car(2L, "Toyota", "", Color.SILVER)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
//        result.getResponse().getContentAsString();
    }

    @Test
    @DirtiesContext
    void should_update_color_test() throws Exception {
//        given
        Car car = new Car(1L, "BMW", "S5", Color.RED);
//        then
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cars/{carId}/color/{newColor}", car.getCarId(), Color.SILVER)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("SILVER"))
                .andReturn();

    }

    @Test
    @DirtiesContext
    void should_not_update_color_id_not_found_test() throws Exception {
        //        given
        Car car = new Car(10L, "BMW", "S5", Color.RED);
        //        then
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cars/{carId}/color/{newColor}", car.getCarId(), Color.SILVER)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void should_update_Mark_test() throws Exception {
        Car car = new Car(1L, "BMW", "S5", Color.RED);

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cars//{id}/mark/{newMark}", car.getCarId(), "BMW")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void should_not_update_Mark_id_not_found_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cars//{id}/mark/{newMark}", 10L, "BMW")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
        @DirtiesContext
    void should_not_update_Mark_empty_mark_parameter_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/cars//{id}/mark/{newMark}", 1L, "")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void should_delete_By_Id_test() throws Exception {
//        Car car = new Car(1L, "Ferrari", "599 GTB Fiorano", Color.RED);
//        Car car2 = carApi.getAll().stream().filter(car1 -> car1.getCarId() == 3L).findFirst().get();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/{id}", 3L))
                .andExpect(status().isAccepted());
    }

    @Test
    @DirtiesContext
    void should_not_delete_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/{id}", 10L))
                .andExpect(status().isNotFound());
    }
}

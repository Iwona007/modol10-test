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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CarApiTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void Should_get_All_Cars_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].mark").value("Ferrari"));
    }

    @Test
    void should_get_car_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/{carId}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value(1L));
    }

    @Test
    void should_not_get_car_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/{carId}", 4L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(containsString("Car with given id: 4 not exist")));
    }

//    @Test
//    void getByColor() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/color/{color}", Color.RED)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status());
//    }

    @Test
    void should_add_new_Car_test() throws Exception {
        Car car = new Car(5L, "Audi", "C5", Color.RED);
        String jsonRequest = objectMapper.writeValueAsString(car);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/cars/new")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").exists());
//                .andReturn();
//        result.getResponse().getContentAsString();
    }

    @Test
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
    void should_modify_Car_with_exist_id_test() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/modify/{id}", 2L)
                .content(asJsonString(new Car(2L, "Toyota", "Rav4", Color.SILVER)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("Toyota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Rav4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("SILVER"))
                .andReturn();
        result.getResponse().getContentAsString();
    }

    @Test
    void should_modify_method_when_id_not_exist_add_new_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/modify/{id}", 2L)
                .content(asJsonString(new Car(5L, "Toyota", "Rav4", Color.SILVER)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark").value("Toyota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Rav4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("SILVER"))
                .andReturn();
//        result.getResponse().getContentAsString();
    }

    @Test
    void should_not_modify_an_existing_car_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/modify/{id}", 2L)
                .content(asJsonString(new Car(5L, "Toyota", "", Color.SILVER)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
//        result.getResponse().getContentAsString();
    }

    @Test
    void updateColor() {
    }

    @Test
    void updateMark() {
    }

    @Test
    void should_delete_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/{id}", 3L))
                .andExpect(status().isAccepted());
    }

    @Test
    void should_not_delete_By_Id_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/{id}", 10L))
                .andExpect(status().isNotFound());
    }

}

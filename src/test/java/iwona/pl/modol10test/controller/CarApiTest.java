package iwona.pl.modol10test.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarApi.class)
class CarApiTest {


    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllCars() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].mark").value("Ferrari"));
    }

    @Test
    void getById() {
    }

    @Test
    void getByColor() {
    }

    @Test
    void addCar() {
    }

    @Test
    void updateColor() {
    }

    @Test
    void updateMark() {
    }

    @Test
    void deleteById() {
    }
}

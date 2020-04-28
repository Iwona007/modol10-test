package iwona.pl.modol10test;

        import iwona.pl.modol10test.controller.CarApi;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;

        import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Modol10TestApplicationTests {

    @Autowired
    private CarApi carApi;

    @Test
    void contextLoads() {
        assertThat(carApi).isNotNull();
    }

}

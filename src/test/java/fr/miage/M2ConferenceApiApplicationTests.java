package fr.miage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class M2ConferenceApiApplicationTests {

    @Test
    void isTrue() {
        assertThat(true, equalTo(true));
    }

}

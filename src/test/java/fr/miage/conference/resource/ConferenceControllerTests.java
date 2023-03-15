package fr.miage.conference.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.resource.ConferenceResource;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConferenceControllerTests {

    @Autowired
    ConferenceResource cr;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setupContext() {
        cr.deleteAll();
        RestAssured.port = port;
    }

    @Test
    void getConference_isExist_ExpectedTrue() throws Exception {

        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur");
        cr.save(conference);

        // ACT
        Response response = when().get("/conferences/"+conference.getId()).then()
                .statusCode(HttpStatus.SC_OK).extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("conferenceName"));
    }

    @Test
    void getConference_isNotExist_ExpectedFalse() throws Exception {

        // ARRANGE

        // ACT & ASSERT
        when().get("/conferences/12").then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

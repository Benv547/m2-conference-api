package fr.miage.conference.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.conference.api.dto.ConferenceInput;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.resource.ConferenceResource;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConferenceControllerTests {

    @Autowired
    ConferenceResource cr;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setupContext() {
        cr.deleteAll();
        RestAssured.port = port;
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void getConference_isExist_ExpectedTrue() throws Exception {

        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", null);
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

    @Test
    void createConference_notAdmin_ExpectedFalse() throws Exception {

        // ARRANGE
        ConferenceInput conferenceInput = new ConferenceInput("conferenceName", "conferenceDescription", "conferencePresentateur");

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))
                .body(this.toJsonString(conferenceInput)).contentType(ContentType.JSON)
                .when().post("/conferences").then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void createConference_isAdmin_ExpectedTrue() throws Exception {

        // ARRANGE
        ConferenceInput conferenceInput = new ConferenceInput("conferenceName", "conferenceDescription", "conferencePresentateur");

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN"))
                .body(this.toJsonString(conferenceInput)).contentType(ContentType.JSON)
                .when().post("/conferences").then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void searchConferenceByTitle_isExist_ExpectedTrue() throws Exception {

        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", null);
        cr.save(conference);

        // ACT
        Response response = when().get("/conferences?search=nom==conferenceName").then()
                .statusCode(HttpStatus.SC_OK).extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("conferenceName"));
    }

    @Test
    void searchConferenceByPresentateur_isExist_ExpectedTrue() throws Exception {

        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", null);
        cr.save(conference);

        // ACT
        Response response = when().get("/conferences?search=presentateur==conferencePresentateur").then()
                .statusCode(HttpStatus.SC_OK).extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("conferencePresentateur"));
    }

    @Test
    void searchPromptIsIncorrect_ExpectedFalse() throws Exception {

        // ARRANGE

        // ACT & ASSERT
        when().get("/conferences?search=machin==conferenceName").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    private String toJsonString(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r);
    }
}

package fr.miage.conference.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.conference.api.dto.SessionInput;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.resource.ConferenceResource;
import fr.miage.conference.session.entity.Session;
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

import java.util.ArrayList;
import java.util.Date;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerTests {

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
    void getSessions_conferenceIdNotExist_ExpectedFalse() throws Exception {
        // ARRANGE

        // ACT & ASSERT
        when().get("/conferences/12/sessions").then()
                .statusCode(HttpStatus.SC_OK)
                .and().assertThat().body("size()",equalTo(1));
    }

    @Test
    void getSession_conferenceIdNotExist_ExpectedFalse() throws Exception {
        // ARRANGE

        // ACT & ASSERT
        when().get("/conferences/12/sessions/12").then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void getSessions_conferenceIdExist_ExpectedTrue() throws Exception {
        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);
        Session session2 = new Session("2", 10.0f, new Date(), "sessionSpeaker2", "1", 50, 50);
        sessions.add(session2);
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        // ACT
        Response response = when().get("/conferences/"+conference.getId()+"/sessions").then()
                .statusCode(HttpStatus.SC_OK)
                .and().assertThat().body("size()",equalTo(2))
                .extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("sessionSpeaker"));
    }

    @Test
    void createSession_conferenceIdNotExist_ExpectedFalse() throws Exception {
        // ARRANGE
        SessionInput sessionInput = new SessionInput(20.0f, new Date(), "sessionSpeaker", 50);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN"))
                .body(this.toJsonString(sessionInput)).contentType(ContentType.JSON)
                .when().post("/conferences/12/sessions").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void createSession_conferenceIdExist_ExpectedTrue() throws Exception {
        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", new ArrayList<>());
        cr.save(conference);

        SessionInput sessionInput = new SessionInput(20.0f, new Date(), "sessionSpeaker", 50);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN"))
                .body(this.toJsonString(sessionInput)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions").then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void createSession_notAdmin_ExpectedFalse() throws Exception {
        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", new ArrayList<>());
        cr.save(conference);

        SessionInput sessionInput = new SessionInput(20.0f, new Date(), "sessionSpeaker", 50);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))
                .body(this.toJsonString(sessionInput)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions").then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void createSession_isAdmin_ExpectedTrue() throws Exception {
        // ARRANGE
        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", new ArrayList<>());
        cr.save(conference);

        SessionInput sessionInput = new SessionInput(20.0f, new Date(), "sessionSpeaker", 50);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN"))
                .body(this.toJsonString(sessionInput)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions").then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    private String toJsonString(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r);
    }
}

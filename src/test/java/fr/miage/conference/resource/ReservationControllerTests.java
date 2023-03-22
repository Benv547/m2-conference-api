package fr.miage.conference.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.conference.api.dto.BankCardInformationInput;
import fr.miage.conference.api.dto.ReservationInput;
import fr.miage.conference.bank.BankService;
import fr.miage.conference.bank.entity.BankCardInformation;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.resource.ConferenceResource;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.resource.ReservationResource;
import fr.miage.conference.session.entity.Session;
import fr.miage.conference.session.resource.SessionResource;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerTests {

    @Autowired
    ReservationResource rr;

    @Autowired
    SessionResource sr;

    @Autowired
    ConferenceResource cr;

    @MockBean
    SecurityContextHolder sch;

    @MockBean
    BankService bs;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setupContext() {
        rr.deleteAll();
        sr.deleteAll();
        cr.deleteAll();
        RestAssured.port = port;
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void getReservation_isExist_ExpectedTrue() {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        // ACT
        MockMvcResponse response = RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .when().get("/conferences/1/sessions/1/reservation/test@test.fr").then()
                .statusCode(HttpStatus.SC_OK).extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("test@test.fr"));
    }

    @Test
    void getReservation_notAuthorize_ExpectedFalse() {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        MockMvcResponse response = RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("pas@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .when().get("/conferences/1/sessions/1/reservation/test@test.fr").then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).extract().response();
    }

    @Test
    void getReservation_notExist_ExpectedNotFound() {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .when().get("/conferences/1/sessions/1/reservation/test@test.fr").then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void createReservation_notExist_ExpectedTrue() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        ReservationInput input = new ReservationInput(3);

        // ACT
        MockMvcResponse response = RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .body(this.toJsonString(input)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr").then()
                .statusCode(HttpStatus.SC_OK).extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("test@test.fr"));
        assertThat(jsonAsString,containsString("\"annulee\":false"));
        assertThat(jsonAsString,containsString("\"payee\":false"));
    }

    @Test
    void createReservation_isExist_ExpectedFalse() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 4, false, false);
        rr.save(reservation);

        ReservationInput input = new ReservationInput(3);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .body(this.toJsonString(input)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void createReservation_notAuthorize_ExpectedFalse() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        ReservationInput input = new ReservationInput(3);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("pas@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .body(this.toJsonString(input)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr").then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    void getAllReservation_asAdmin_ExpectedTrue() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        // ACT
        MockMvcResponse response = RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.fr").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .when().get("/conferences/1/sessions/1/reservation")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response();
        String jsonAsString = response.asString();

        // ASSERT
        assertThat(jsonAsString,containsString("test@test.fr"));
    }

    @Test
    void getAllReservation_asUser_ExpectedTrue() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        // ACT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .when().get("/conferences/1/sessions/1/reservation")
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }


    @Test
    void cancelReservation_Exist_ExpectedTrue() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        // ACT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr/cancel")
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void cancelReservation_ExistAndNotAuth_ExpectedFalse() {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        // ACT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("pas@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr/cancel")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void paymentReservation_Exist_ExpectTrue() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        Reservation reservation = new Reservation("1", "test@test.fr", "1", "1", 40, false, false);
        rr.save(reservation);

        when(bs.processPayment(Mockito.any(BankCardInformation.class), Mockito.anyFloat()))
                .thenReturn(true);

        BankCardInformationInput input = new BankCardInformationInput("1234-1234-1234-1234", "12/2013", "123");

        // ACT
        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .body(this.toJsonString(input)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr/payment")
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

    }

    @Test
    void paymentReservation_ExistAndNotAuth_ExpectFalse() throws Exception {

        // ARRANGE
        ArrayList<Session> sessions = new ArrayList<>();
        Session session = new Session("1", 20.0f, new Date(), "sessionSpeaker", "1", 50, 50);
        sessions.add(session);

        Conference conference = new Conference("1", "conferenceName", "conferenceDescription", "conferencePresentateur", sessions);
        cr.save(conference);

        BankCardInformationInput input = new BankCardInformationInput("1234-1234-1234-1234", "12/2013", "123");

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("pas@test.fr").roles("USER"))
                .contentType(ContentType.JSON)
                .body(this.toJsonString(input)).contentType(ContentType.JSON)
                .when().post("/conferences/1/sessions/1/reservation/test@test.fr/payment")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);

    }
    private String toJsonString(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r);
    }
}

package fr.miage.conference.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.conference.api.dto.UserInput;
import fr.miage.conference.keycloak.KeycloakService;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KeycloakControllerTests {

    @LocalServerPort
    int port;

    @MockBean
    KeycloakService ks;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setupContext() {
        RestAssured.port = port;
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void createUser_isNotExist_ExpectedTrue() throws Exception {

        // ARRANGE
        UserInput userInput = new UserInput("test", "test", "test", "test", "test");

        when(ks.createUser(userInput)).thenReturn(true);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(toJsonString(userInput))
                .when()
                .post("/user")
                .then()
                .statusCode(204);
    }

    @Test
    void createUser_isExist_ExpectedFalse() throws Exception {

        // ARRANGE
        UserInput userInput = new UserInput("test", "test", "test", "test", "test");

        when(ks.createUser(userInput)).thenReturn(false);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(toJsonString(userInput))
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    void createUser_badParamsBlank_ExpectedFalse() throws Exception {

        // ARRANGE
        UserInput userInput = new UserInput("", "", "", "", "");

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(toJsonString(userInput))
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    void createUser_badParamsNull_ExpectedFalse() throws Exception {

        // ARRANGE
        UserInput userInput = new UserInput(null, null, null, null, null);

        // ACT & ASSERT
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(toJsonString(userInput))
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    private String toJsonString(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r);
    }

}

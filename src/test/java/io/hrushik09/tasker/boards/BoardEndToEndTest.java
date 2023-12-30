package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.users.UserDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@EndToEndTest
public class BoardEndToEndTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private EndToEndTestDataPersister dataPersister;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateBoardSuccessfully() {
        UserDTO savedUser = dataPersister.havingPersistedUser();

        given()
                .contentType(ContentType.JSON)
                .queryParam("userId", savedUser.id())
                .body("""
                        {
                        "title": "Board 1"
                        }
                        """)
                .when()
                .post("/api/boards")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Board 1"));
    }
}

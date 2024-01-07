package io.hrushik09.tasker.users;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
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
public class UserEndToEndTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private EndToEndTestDataPersister having;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateUserSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "name": "user 1"
                        }
                        """)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("user 1"));
    }

    @Test
    void shouldFindUserSuccessfully() {
        CreateUserResponse savedUser = having.persistedUser("user 2");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/{id}", savedUser.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(savedUser.id()))
                .body("name", equalTo("user 2"))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }
}

package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.users.CreateUserCommand;
import io.hrushik09.tasker.users.UserDTO;
import io.hrushik09.tasker.users.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@EndToEndTest
public class ListEndToEndTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private ListService listService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private UserDTO havingPersistedUser() {
        return userService.create(new CreateUserCommand("Not important"));
    }

    private ListDTO havingPersistedList(String title, int userId) {
        return listService.create(new CreateListCommand(title, userId));
    }

    @Test
    void shouldCreateListSuccessfully() {
        havingPersistedUser();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "title": "To Do",
                        "userId": 1
                        }
                        """)
                .when()
                .post("/api/lists")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("To Do"));
    }

    @Test
    void shouldFetchAllListsForGivenUser() {
        UserDTO userDTO = havingPersistedUser();
        ListDTO toDo = havingPersistedList("To Do", userDTO.id());
        ListDTO completed = havingPersistedList("Completed", userDTO.id());
        ListDTO deployed = havingPersistedList("Deployed", userDTO.id());

        given()
                .contentType(ContentType.JSON)
                .param("userId", userDTO.id())
                .when()
                .get("/api/lists")
                .then()
                .statusCode(200)
                .body("lists", hasSize(3))
                .body("lists.id", containsInAnyOrder(toDo.id(), completed.id(), deployed.id()))
                .body("lists.title", containsInAnyOrder(toDo.title(), completed.title(), deployed.title()));
    }
}

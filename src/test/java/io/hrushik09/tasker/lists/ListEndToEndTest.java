package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.boards.CreateBoardResponse;
import io.hrushik09.tasker.users.CreateUserResponse;
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
    private EndToEndTestDataPersister dataPersister;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateListSuccessfully() {
        CreateUserResponse savedUser = dataPersister.havingPersistedUser();
        CreateBoardResponse savedBoard = dataPersister.havingPersistedBoard(savedUser.id());

        given()
                .contentType(ContentType.JSON)
                .queryParam("boardId", savedBoard.id())
                .body("""
                        {
                        "title": "To Do"
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
    void shouldFetchAllListsForGivenBoard() {
        CreateUserResponse savedUser = dataPersister.havingPersistedUser();
        CreateBoardResponse savedBoard = dataPersister.havingPersistedBoard(savedUser.id());
        ListDTO toDo = dataPersister.havingPersistedList("To Do", savedBoard.id());
        ListDTO completed = dataPersister.havingPersistedList("Completed", savedBoard.id());
        ListDTO deployed = dataPersister.havingPersistedList("Deployed", savedBoard.id());

        given()
                .contentType(ContentType.JSON)
                .param("boardId", savedBoard.id())
                .when()
                .get("/api/lists")
                .then()
                .statusCode(200)
                .body("lists", hasSize(3))
                .body("lists.id", containsInAnyOrder(toDo.id(), completed.id(), deployed.id()))
                .body("lists.title", containsInAnyOrder(toDo.title(), completed.title(), deployed.title()));
    }

    @Test
    void shouldUpdateListTitleSuccessfully() {
        CreateUserResponse savedUser = dataPersister.havingPersistedUser();
        CreateBoardResponse savedBoard = dataPersister.havingPersistedBoard(savedUser.id());
        ListDTO listDTO = dataPersister.havingPersistedList("Original List title", savedBoard.id());

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "title": "New List title"
                        }
                        """)
                .when()
                .put("/api/lists/{id}", listDTO.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(listDTO.id()))
                .body("title", equalTo("New List title"));
    }
}

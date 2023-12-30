package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.boards.BoardDTO;
import io.hrushik09.tasker.users.UserDTO;
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
        UserDTO userDTO = dataPersister.havingPersistedUser();
        BoardDTO boardDTO = dataPersister.havingPersistedBoard(userDTO.id());

        given()
                .contentType(ContentType.JSON)
                .queryParam("boardId", boardDTO.id())
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
        UserDTO userDTO = dataPersister.havingPersistedUser();
        BoardDTO boardDTO = dataPersister.havingPersistedBoard(userDTO.id());
        ListDTO toDo = dataPersister.havingPersistedList("To Do", boardDTO.id());
        ListDTO completed = dataPersister.havingPersistedList("Completed", boardDTO.id());
        ListDTO deployed = dataPersister.havingPersistedList("Deployed", boardDTO.id());

        given()
                .contentType(ContentType.JSON)
                .param("boardId", boardDTO.id())
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
        UserDTO userDTO = dataPersister.havingPersistedUser();
        BoardDTO boardDTO = dataPersister.havingPersistedBoard(userDTO.id());
        ListDTO listDTO = dataPersister.havingPersistedList("Original List title", boardDTO.id());

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

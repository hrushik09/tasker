package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.cards.CardMinDTO;
import io.hrushik09.tasker.lists.CreateListResponse;
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
        CreateUserResponse savedUser = dataPersister.havingPersistedUser();

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

    @Test
    void shouldFetchAllDataForGivenBoardSuccessfully() {
        CreateBoardResponse board = dataPersister.havingPersistedBoard();
        CreateListResponse working = dataPersister.havingPersistedList("Working", board.id());
        CreateListResponse completed = dataPersister.havingPersistedList("Completed", board.id());
        CardMinDTO card = dataPersister.havingPersistedCard("Card 1", working.id());
        CardMinDTO documentation = dataPersister.havingPersistedCard("Documentation", completed.id());
        CardMinDTO formatting = dataPersister.havingPersistedCard("Formatting", working.id());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/boards/{id}", board.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(board.id()))
                .body("lists", hasSize(2))
                .body("lists.id", containsInAnyOrder(working.id(), completed.id()))
                .body("lists.title", containsInAnyOrder(working.title(), completed.title()))
                .body("cards", hasSize(3))
                .body("cards.id", containsInAnyOrder(card.id(), documentation.id(), formatting.id()))
                .body("cards.title", containsInAnyOrder(card.title(), documentation.title(), formatting.title()))
                .body("cards.listId", containsInAnyOrder(card.listId(), documentation.listId(), formatting.listId()));
    }
}

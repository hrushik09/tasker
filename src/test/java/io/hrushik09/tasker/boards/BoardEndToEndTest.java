package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.cards.CreateCardResponse;
import io.hrushik09.tasker.lists.CreateListResponse;
import io.hrushik09.tasker.users.CreateUserResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@EndToEndTest
public class BoardEndToEndTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private EndToEndTestDataPersister having;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateBoardSuccessfully() {
        CreateUserResponse savedUser = having.persistedUser();

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
        CreateBoardResponse board = having.persistedBoard();
        CreateListResponse working = having.persistedList("Working", board.id());
        CreateListResponse completed = having.persistedList("Completed", board.id());
        CreateCardResponse card = having.persistedCard("Card 1", working.id());
        CreateCardResponse documentation = having.persistedCard("Documentation", completed.id());
        CreateCardResponse formatting = having.persistedCard("Formatting", working.id());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/boards/{id}", board.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(board.id()))
                .body("lists", hasSize(2))
                .body("lists.id", contains(working.id(), completed.id()))
                .body("lists.title", contains(working.title(), completed.title()))
                .body("cards", hasSize(3))
                .body("cards.id", contains(card.id(), documentation.id(), formatting.id()))
                .body("cards.title", contains(card.title(), documentation.title(), formatting.title()))
                .body("cards.listId", contains(card.listId(), documentation.listId(), formatting.listId()));
    }

    @Test
    void shouldNotFetchUnarchivedCards() {
        CreateBoardResponse board = having.persistedBoard();
        CreateListResponse working = having.persistedList("Working", board.id());
        CreateCardResponse card = having.persistedCard("Card 1", working.id());
        CreateCardResponse documentation = having.persistedCard("Documentation", working.id());
        CreateCardResponse formatting = having.persistedCard("Formatting", working.id());
        Map<String, Object> fields = Map.of("archived", true);
        having.updatedCard(documentation.id(), fields);
        having.updatedCard(formatting.id(), fields);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/boards/{id}", board.id())
                .then()
                .body("cards", hasSize(1))
                .body("cards.id", contains(card.id()));
    }
}

package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.boards.CreateBoardResponse;
import io.hrushik09.tasker.lists.CreateListResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@EndToEndTest
public class CardEndToEndTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private EndToEndTestDataPersister having;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateCardSuccessfully() {
        CreateListResponse savedList = having.persistedList();

        given()
                .contentType(ContentType.JSON)
                .queryParam("listId", savedList.id())
                .body("""
                        {
                        "title": "Card 1"
                        }
                        """)
                .when()
                .post("/api/cards")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Card 1"))
                .body("listId", equalTo(savedList.id()));
    }

    @Nested
    class FetchCardDetails {
        @Test
        void shouldFetchBasicCardDetailsSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(card.id()))
                    .body("title", equalTo(card.title()))
                    .body("listId", equalTo(card.listId()))
                    .body("createdAt", notNullValue())
                    .body("updatedAt", notNullValue());
        }

        @Test
        void shouldFetchUpdatedTitleSuccessfully() {
            CreateCardResponse createdCard = having.persistedCard();
            Map<String, Object> fields = Map.of("title", "The new title");
            having.updatedCard(createdCard.id(), fields);

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/cards/{id}", createdCard.id())
                    .then()
                    .body("title", equalTo("The new title"));
        }

        @Test
        void shouldFetchUpdatedDescriptionSuccessfully() {
            CreateCardResponse createdCard = having.persistedCard();
            Map<String, Object> fields = Map.of("description", "This is updated card description");
            having.updatedCard(createdCard.id(), fields);

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/cards/{id}", createdCard.id())
                    .then()
                    .body("description", equalTo("This is updated card description"));
        }

        @Test
        void shouldFetchUpdatedStartSuccessfully() {
            CreateCardResponse createdCard = having.persistedCard();
            Map<String, Object> fields = Map.of("start", "2023-02-02T12:12:12Z");
            having.updatedCard(createdCard.id(), fields);

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/cards/{id}", createdCard.id())
                    .then()
                    .body("start", equalTo("2023-02-02T12:12:12Z"));
        }

        @Test
        void shouldFetchUpdatedDueSuccessfully() {
            CreateCardResponse createdCard = having.persistedCard();
            Map<String, Object> fields = Map.of("due", "2023-02-05T12:12:12Z");
            having.updatedCard(createdCard.id(), fields);

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/cards/{id}", createdCard.id())
                    .then()
                    .body("due", equalTo("2023-02-05T12:12:12Z"));
        }
    }

    @Nested
    class UpdateCard {
        @Test
        void shouldReturnCorrectResponseFieldsAfterAllowedFieldUpdateIsPerformed() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "description": "Not important"
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(card.id()))
                    .body("title", equalTo(card.title()))
                    .body("listId", equalTo(card.listId()));
        }

        @Test
        void shouldUpdateTitleSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "title": "This is the updated title"
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200)
                    .body("title", equalTo("This is the updated title"));
        }

        @Test
        void shouldUpdateDescriptionSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "description": "This is updated description"
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200);
        }

        @Test
        void shouldUpdateStartSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "start": "2023-12-20T14:35:23Z"
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200);
        }

        @Test
        void shouldUpdateDueSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "due": "2023-12-22T14:35:23Z"
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200);
        }

        @Test
        void shouldArchiveCardSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "archived": true
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200);
        }

        @Test
        void shouldUnarchiveCardSuccessfully() {
            CreateCardResponse card = having.persistedCard();

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "archived": false
                            }
                            """)
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200);
        }

        @Test
        void shouldMoveCardToDifferentList() {
            CreateBoardResponse board = having.persistedBoard();
            CreateListResponse toDo = having.persistedList("ToDo", board.id());
            CreateListResponse completed = having.persistedList("Completed", board.id());
            CreateCardResponse card = having.persistedCard("Not important", toDo.id());

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                            "list": %s
                            }
                            """.formatted(toDo.id()))
                    .when()
                    .patch("/api/cards/{id}", card.id())
                    .then()
                    .statusCode(200)
                    .body("listId", equalTo(completed.id()));
        }
    }
}

package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
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
    private EndToEndTestDataPersister dataPersister;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateCardSuccessfully() {
        CreateListResponse savedList = dataPersister.havingPersistedList();

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

    @Test
    void shouldFetchCardDetailsSuccessfully() {
        CreateCardResponse createdCard = dataPersister.havingPersistedCard();
        Map<String, Object> fields = Map.of("description", "This is updated card description");
        UpdateCardResponse updatedCard = dataPersister.havingUpdatedCardDescription(createdCard.id(), fields);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/cards/{id}", updatedCard.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(updatedCard.id()))
                .body("title", equalTo(updatedCard.title()))
                .body("description", equalTo("This is updated card description"))
                .body("listId", equalTo(updatedCard.listId()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Nested
    class UpdateCard {
        @Test
        void shouldReturnCorrectResponseFieldsAfterAllowedFieldUpdateIsPerformed() {
            CreateCardResponse card = dataPersister.havingPersistedCard();

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
        void shouldUpdateDescriptionSuccessfully() {
            CreateCardResponse card = dataPersister.havingPersistedCard();

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
        void shouldUpdateStartTimeSuccessfully() {
            CreateCardResponse card = dataPersister.havingPersistedCard();

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
    }
}

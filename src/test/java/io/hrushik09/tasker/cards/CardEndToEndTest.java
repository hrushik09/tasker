package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.lists.CreateListResponse;
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
                .put("/api/cards/{id}", card.id())
                .then()
                .statusCode(200)
                .body("description", equalTo("This is updated description"))
                .body("id", equalTo(card.id()))
                .body("title", equalTo(card.title()))
                .body("listId", equalTo(card.listId()));
    }

    @Test
    void shouldFetchCardDetailsSuccessfully() {
        CreateCardResponse createdCard = dataPersister.havingPersistedCard();
        UpdateCardDescriptionResponse updatedCard = dataPersister.havingUpdatedCardDescription(createdCard.id(), "This is updated card description");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/cards/{id}", updatedCard.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(updatedCard.id()))
                .body("title", equalTo(updatedCard.title()))
                .body("description", equalTo(updatedCard.description()));
    }
}

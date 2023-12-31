package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.EndToEndTest;
import io.hrushik09.tasker.EndToEndTestDataPersister;
import io.hrushik09.tasker.lists.ListDTO;
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
        ListDTO listDTO = dataPersister.havingPersistedList();

        given()
                .contentType(ContentType.JSON)
                .queryParam("listId", listDTO.id())
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
                .body("listId", equalTo(listDTO.id()));
    }

    @Test
    void shouldUpdateDescriptionSuccessfully() {
        CardDTO cardDTO = dataPersister.havingPersistedCard();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "description": "This is updated description"
                        }
                        """)
                .when()
                .put("/api/cards/{id}", cardDTO.id())
                .then()
                .statusCode(200)
                .body("description", equalTo("This is updated description"))
                .body("id", equalTo(cardDTO.id()))
                .body("title", equalTo(cardDTO.title()))
                .body("listId", equalTo(cardDTO.listId()));
    }
}

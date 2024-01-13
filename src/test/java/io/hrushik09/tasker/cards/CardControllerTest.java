package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListDoesNotExistException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static io.hrushik09.tasker.cards.CardMaxDetailsDTOBuilder.aCardMaxDetailsDTO;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CardController.class)
public class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;

    @Test
    void shouldFailToCreateCardForNonExistingListId() throws Exception {
        when(cardService.create(new CreateCardCommand(100, "Not important"))).thenThrow(new ListDoesNotExistException(100));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("listId", String.valueOf(100))
                        .content("""
                                {
                                "title": "Not important"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("List with id=100 does not exist")));
    }

    @Test
    void shouldCreateCardSuccessfully() throws Exception {
        when(cardService.create(new CreateCardCommand(1, "Card 1")))
                .thenReturn(new CreateCardResponse(1, "Card 1", 1));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("listId", String.valueOf(1))
                        .content("""
                                {
                                "title": "Card 1"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("Card 1")))
                .andExpect(jsonPath("$.listId", equalTo(1)));
    }

    @Nested
    class FetchCardDetails {
        @Test
        void shouldThrowWhenFetchingCardDetailsForNonExistingCard() throws Exception {
            Integer nonExistingId = 101;
            when(cardService.fetchCardDetails(nonExistingId)).thenThrow(new CardDoesNotExistException(nonExistingId));

            mockMvc.perform(get("/api/cards/{id}", nonExistingId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", equalTo("Card with id=" + nonExistingId + " does not exist")));
        }

        @Test
        void shouldFetchCardDetailsSuccessfully() throws Exception {
            Integer id = 1;
            String title = "Custom card";
            String description = "This is the current description";
            Integer listId = 2;
            String startStr = "2024-01-01T22:23:23Z";
            String dueStr = "2024-01-04T22:23:23Z";
            CardMaxDetailsDTOBuilder cardMaxDetailsDTOBuilder = aCardMaxDetailsDTO().withId(id).withTitle(title).withDescription(description).withStart(Instant.parse(startStr))
                    .withDue(Instant.parse(dueStr)).withListId(listId).withCreatedAt(Instant.now())
                    .withUpdatedAt(Instant.now());
            when(cardService.fetchCardDetails(id)).thenReturn(cardMaxDetailsDTOBuilder.build());

            mockMvc.perform(get("/api/cards/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(id)))
                    .andExpect(jsonPath("$.title", equalTo(title)))
                    .andExpect(jsonPath("$.description", equalTo(description)))
                    .andExpect(jsonPath("$.start", equalTo(startStr)))
                    .andExpect(jsonPath("$.due", equalTo(dueStr)))
                    .andExpect(jsonPath("$.listId", equalTo(listId)))
                    .andExpect(jsonPath("$.createdAt", notNullValue()))
                    .andExpect(jsonPath("$.updatedAt", notNullValue()));
        }

        @Nested
        class FetchCardActionDetails {
            @Test
            void shouldFetchCreateCardActionDetails() throws Exception {
                Integer creatorId = 10;
                String creatorName = "User 1";
                Integer listId = 23;
                String listTitle = "Completed";
                Integer id = 1;
                String cardTitle = "Card 1 Title";
                CardMaxDetailsDTOBuilder cardMaxDetailsDTOBuilder = aCardMaxDetailsDTO()
                        .withActions(List.of(new Action(123, creatorId, "createCard", Instant.parse("2023-12-12T12:23:44Z"),
                                new ActionDisplay("action_create_card",
                                        new ActionDisplayEntities(
                                                new CardActionEntity("card", id, cardTitle),
                                                new ListActionEntity("list", listId, listTitle),
                                                new MemberCreatorActionEntity("member", creatorId, creatorName)
                                        ))
                        )));
                when(cardService.fetchCardDetails(id)).thenReturn(cardMaxDetailsDTOBuilder.build());

                mockMvc.perform(get("/api/cards/{id}", id))
                        .andExpect(jsonPath("$.actions", hasSize(1)))
                        .andExpect(jsonPath("$.actions[0].id", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].memberCreatorId", equalTo(creatorId)))
                        .andExpect(jsonPath("$.actions[0].type", equalTo("createCard")))
                        .andExpect(jsonPath("$.actions[0].happenedAt", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].display", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].display.translationKey", equalTo("action_create_card")))
                        .andExpect(jsonPath("$.actions[0].display.entities", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].display.entities.card", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].display.entities.card.type", equalTo("card")))
                        .andExpect(jsonPath("$.actions[0].display.entities.card.id", equalTo(id)))
                        .andExpect(jsonPath("$.actions[0].display.entities.card.text", equalTo(cardTitle)))
                        .andExpect(jsonPath("$.actions[0].display.entities.list", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].display.entities.list.type", equalTo("list")))
                        .andExpect(jsonPath("$.actions[0].display.entities.list.id", equalTo(listId)))
                        .andExpect(jsonPath("$.actions[0].display.entities.list.text", equalTo(listTitle)))
                        .andExpect(jsonPath("$.actions[0].display.entities.memberCreator", notNullValue()))
                        .andExpect(jsonPath("$.actions[0].display.entities.memberCreator.type", equalTo("member")))
                        .andExpect(jsonPath("$.actions[0].display.entities.memberCreator.id", equalTo(creatorId)))
                        .andExpect(jsonPath("$.actions[0].display.entities.memberCreator.text", equalTo(creatorName)));
            }
        }
    }

    @Nested
    class UpdateCard {
        @Test
        void shouldThrowWhenUpdatingNonExistingCard() throws Exception {
            Integer nonExistingId = 100;
            when(cardService.update(any())).thenThrow(new CardDoesNotExistException(nonExistingId));

            mockMvc.perform(patch("/api/cards/{id}", nonExistingId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "description": "Not important"
                                    }
                                    """))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error",
                            equalTo("Card with id=" + nonExistingId + " does not exist")));
        }

        @Test
        void shouldThrowWhenUpdatingNonExistingField() throws Exception {
            when(cardService.update(any())).thenThrow(new InvalidFieldForUpdateCardException("invalidFieldName"));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "invalidFieldName": "Not important"
                                    }
                                    """))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", equalTo("Field invalidFieldName not found in Card")));
        }

        @Test
        void shouldReturnCorrectResponseFieldsAfterAllowedFieldUpdateIsPerformed() throws Exception {
            Map<String, Object> fields = Map.of("description", "Not important");
            when(cardService.update(new UpdateCardCommand(1, fields))).thenReturn(new UpdateCardResponse(1, "Not important", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "description": "Not important"
                                    }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(1)))
                    .andExpect(jsonPath("$.title", equalTo("Not important")))
                    .andExpect(jsonPath("$.listId", equalTo(1)));
        }

        @Test
        void shouldUpdateTitleSuccessfully() throws Exception {
            Map<String, Object> fields = Map.of("title", "This is updated title");
            when(cardService.update(new UpdateCardCommand(1, fields)))
                    .thenReturn(new UpdateCardResponse(1, "This is updated title", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "title": "This is updated title"
                                    }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", equalTo("This is updated title")));
        }

        @Test
        void shouldUpdateDescriptionSuccessfully() throws Exception {
            Map<String, Object> fields = Map.of("description", "Description after update");
            when(cardService.update(new UpdateCardCommand(1, fields))).thenReturn(new UpdateCardResponse(1, "Not important", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "description": "Description after update"
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldUpdateStartSuccessfully() throws Exception {
            Map<String, Object> fields = Map.of("start", "2023-02-14T23:45:45Z");
            when(cardService.update(new UpdateCardCommand(1, fields))).thenReturn(new UpdateCardResponse(1, "Not important", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "start": "2023-02-14T23:45:45Z"
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldUpdateDueSuccessfully() throws Exception {
            Map<String, Object> fields = Map.of("due", "2023-02-17T23:45:45Z");
            when(cardService.update(new UpdateCardCommand(1, fields)))
                    .thenReturn(new UpdateCardResponse(1, "Not important", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "due": "2023-02-17T23:45:45Z"
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldArchiveCardSuccessfully() throws Exception {
            Map<String, Object> fields = Map.of("archived", true);
            when(cardService.update(new UpdateCardCommand(1, fields)))
                    .thenReturn(new UpdateCardResponse(1, "Not important", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "archived": true
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldUnarchiveCardSuccessfully() throws Exception {
            Map<String, Object> fields = Map.of("archived", false);
            when(cardService.update(new UpdateCardCommand(1, fields)))
                    .thenReturn(new UpdateCardResponse(1, "Not important", 1));

            mockMvc.perform(patch("/api/cards/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "archived": false
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Nested
        class MoveCardToAnotherList {
            @Test
            void shouldThrowWhenMovingCardToNonExistingList() throws Exception {
                Map<String, Object> fields = Map.of("list", 100);
                when(cardService.update(new UpdateCardCommand(1, fields))).thenThrow(new ListDoesNotExistException(100));

                mockMvc.perform(patch("/api/cards/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "list": 100
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error", equalTo("List with id=100 does not exist")));
            }

            @Test
            void shouldThrowWhenMovingCardToListNotInCurrentBoard() throws Exception {
                Map<String, Object> fields = Map.of("list", 100);
                when(cardService.update(new UpdateCardCommand(1, fields))).thenThrow(new ListNotInGivenBoardException(100));

                mockMvc.perform(patch("/api/cards/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "list": 100
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error", equalTo("List with id=100 does not exist in current board")));
            }

            @Test
            void shouldMoveCardToDifferentListSuccessfully() throws Exception {
                Map<String, Object> fields = Map.of("list", 2);
                when(cardService.update(new UpdateCardCommand(1, fields)))
                        .thenReturn(new UpdateCardResponse(1, "Not important", 2));

                mockMvc.perform(patch("/api/cards/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "list": 2
                                        }
                                        """))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.listId", equalTo(2)));
            }
        }

        @Nested
        class NotAllowedFieldsForUpdate {
            @Test
            void shouldThrowWhenUpdatingFieldId() throws Exception {
                when(cardService.update(any())).thenThrow(new NotAllowedFieldForUpdateCardException("id"));

                mockMvc.perform(patch("/api/cards/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "id": 100
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error", equalTo("Field id is not allowed for update")));
            }

            @Test
            void shouldThrowWhenUpdatingFieldCreatedAt() throws Exception {
                when(cardService.update(any())).thenThrow(new NotAllowedFieldForUpdateCardException("createdAt"));

                mockMvc.perform(patch("/api/cards/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "createdAt": "Not important"
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error", equalTo("Field createdAt is not allowed for update")));
            }

            @Test
            void shouldThrowWhenUpdatingFieldUpdatedAt() throws Exception {
                when(cardService.update(any())).thenThrow(new NotAllowedFieldForUpdateCardException("updatedAt"));

                mockMvc.perform(patch("/api/cards/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "updatedAt": "Not important"
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error", equalTo("Field updatedAt is not allowed for update")));
            }
        }
    }
}

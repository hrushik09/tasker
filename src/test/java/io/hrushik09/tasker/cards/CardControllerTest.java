package io.hrushik09.tasker.cards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
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
    void shouldCreateCardSuccessfully() throws Exception {
        when(cardService.create(new CreateCardCommand(1, "Card 1"))).thenReturn(new CreateCardResponse(1, "Card 1", 1));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("listId", String.valueOf(1))
                        .content("""
                                {
                                "title": "Card 1"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("Card 1")))
                .andExpect(jsonPath("$.listId", equalTo(1)));
    }

    @Test
    void shouldThrowWhenUpdatingDescriptionForNonExistingCard() throws Exception {
        Integer nonExistingId = 100;
        when(cardService.updateDescription(any())).thenThrow(new CardDoesNotExistException(nonExistingId));

        mockMvc.perform(put("/api/cards/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "Not important"
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("Card with id=" + nonExistingId + " does not exist")));
    }

    @Test
    void shouldUpdateDescriptionSuccessfully() throws Exception {
        when(cardService.updateDescription(new UpdateDescriptionCommand(1, "Description after update"))).thenReturn(new UpdateCardDescriptionResponse(1, "Not important", 1, "Description after update"));

        mockMvc.perform(put("/api/cards/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "Description after update"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo("Description after update")))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("Not important")))
                .andExpect(jsonPath("$.listId", equalTo(1)));
    }

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
        when(cardService.fetchCardDetails(id)).thenReturn(new CardMaxDetailsDTO(id, title, description, listId, Instant.now(), Instant.now()));

        mockMvc.perform(get("/api/cards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id)))
                .andExpect(jsonPath("$.title", equalTo(title)))
                .andExpect(jsonPath("$.description", equalTo(description)))
                .andExpect(jsonPath("$.listId", equalTo(listId)))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }
}

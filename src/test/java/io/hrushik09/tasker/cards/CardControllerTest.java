package io.hrushik09.tasker.cards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        when(cardService.create(new CreateCardCommand(1, "Card 1"))).thenReturn(new CardMinDTO(1, "Card 1", 1));

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
    void shouldUpdateDescriptionSuccessfully() throws Exception {
        when(cardService.updateDescription(new UpdateDescriptionCommand(1, "Description after update"))).thenReturn(new CardDTO(1, "Not important", 1, "Description after update"));

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
}

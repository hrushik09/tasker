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
        when(cardService.create(new CreateCardCommand(1, "Card 1"))).thenReturn(new CardDTO(1, "Card 1", 1, null));

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
        Integer id = 1;
        String title = "Not important";
        Integer listId = 1;
        String updatedDescription = "Description after update";
        when(cardService.updateDescription(new UpdateDescriptionCommand(id, updatedDescription))).thenReturn(new CardDTO(id, title, listId, updatedDescription));

        mockMvc.perform(put("/api/cards/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "Description after update"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo(updatedDescription)))
                .andExpect(jsonPath("$.id", equalTo(id)))
                .andExpect(jsonPath("$.title", equalTo(title)))
                .andExpect(jsonPath("$.listId", equalTo(listId)));
    }
}

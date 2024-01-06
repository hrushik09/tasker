package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.cards.CardMinDetailsDTO;
import io.hrushik09.tasker.lists.ListDetailsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class)
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BoardService boardService;
    @MockBean
    private BoardDataService boardDataService;

    @Test
    void shouldCreateBoardSuccessfully() throws Exception {
        when(boardService.create(new CreateBoardCommand("Development Board", 1)))
                .thenReturn(new CreateBoardResponse(1, "Development Board"));

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("userId", String.valueOf(1))
                        .content("""
                                {
                                "title": "Development Board"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("Development Board")));
    }

    @Test
    void shouldThrowWhenFetchingAllDataForNonExistingBoard() throws Exception {
        Integer nonExistingId = 1;
        when(boardDataService.fetchAllData(new FetchBoardDataQuery(nonExistingId))).thenThrow(new BoardDoesNotExistException(nonExistingId));

        mockMvc.perform(get("/api/boards/{id}", nonExistingId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("Board with id=" + nonExistingId + " does not exist")));
    }

    @Test
    void shouldFetchAllDataForGivenBoardSuccessfully() throws Exception {
        Integer boardId = 1;
        List<ListDetailsDTO> lists = List.of(
                new ListDetailsDTO(1, "Future Works"),
                new ListDetailsDTO(2, "Working"),
                new ListDetailsDTO(3, "Completed"),
                new ListDetailsDTO(4, "Deployed")
        );
        List<CardMinDetailsDTO> cards = List.of(
                new CardMinDetailsDTO(1, "Card 1", 1),
                new CardMinDetailsDTO(2, "Temp", 2),
                new CardMinDetailsDTO(3, "Card 2", 4),
                new CardMinDetailsDTO(4, "Documentation", 2),
                new CardMinDetailsDTO(5, "Formatting", 3),
                new CardMinDetailsDTO(6, "New features", 1),
                new CardMinDetailsDTO(7, "Refactoring", 3)
        );
        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO(boardId, lists, cards);
        when(boardDataService.fetchAllData(new FetchBoardDataQuery(boardId))).thenReturn(boardDetailsDTO);

        mockMvc.perform(get("/api/boards/{id}", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(boardId)))
                .andExpect(jsonPath("$.lists", hasSize(4)))
                .andExpect(jsonPath("$.lists[*].id", containsInAnyOrder(1, 2, 3, 4)))
                .andExpect(jsonPath("$.lists[*].title", containsInAnyOrder("Future Works", "Working", "Completed", "Deployed")))
                .andExpect(jsonPath("$.cards", hasSize(7)))
                .andExpect(jsonPath("$.cards[*].id", containsInAnyOrder(1, 2, 3, 4, 5, 6, 7)))
                .andExpect(jsonPath("$.cards[*].title", containsInAnyOrder("Card 1", "Temp", "Card 2", "Documentation", "Formatting", "New features", "Refactoring")))
                .andExpect(jsonPath("$.cards[*].listId", containsInAnyOrder(1, 2, 4, 2, 3, 1, 3)));
    }
}

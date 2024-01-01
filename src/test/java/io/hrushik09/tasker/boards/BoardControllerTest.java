package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.lists.ListDTO;
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

    @Test
    void shouldCreateBoardSuccessfully() throws Exception {
        Integer userId = 1;
        when(boardService.create(new CreateBoardCommand("Development Board", userId)))
                .thenReturn(new BoardDTO(1, "Development Board"));

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("userId", String.valueOf(userId))
                        .content("""
                                {
                                "title": "Development Board"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("Development Board")));
    }

    @Test
    void shouldFetchAllDataForGivenBoard() throws Exception {
        Integer boardId = 1;
        List<ListDTO> lists = List.of(
                new ListDTO(1, "Future Works"),
                new ListDTO(2, "Working"),
                new ListDTO(3, "Completed"),
                new ListDTO(4, "Deployed")
        );
        List<CardMinDTO> cards = List.of(
                new CardMinDTO(1, "Card 1", 1),
                new CardMinDTO(2, "Temp", 2),
                new CardMinDTO(3, "Card 2", 4),
                new CardMinDTO(4, "Documentation", 2),
                new CardMinDTO(5, "Formatting", 3),
                new CardMinDTO(6, "New features", 1),
                new CardMinDTO(7, "Refactoring", 3)
        );
        BoardDataDTO boardDataDTO = new BoardDataDTO(boardId, lists, cards);
        when(boardService.fetchAllData(new FetchBoardDataQuery(boardId))).thenReturn(boardDataDTO);

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

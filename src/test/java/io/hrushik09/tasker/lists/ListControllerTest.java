package io.hrushik09.tasker.lists;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListController.class)
public class ListControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ListService listService;

    @Test
    void shouldCreateListSuccessfully() throws Exception {
        when(listService.create(new CreateListCommand("To Do", 1))).thenReturn(new ListDTO(1, "To Do"));

        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("boardId", String.valueOf(1))
                        .content("""
                                {
                                "title": "To Do"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("To Do")));
    }

    @Test
    void shouldFetchAllListsForGivenBoard() throws Exception {
        Integer boardId = 1;
        AllListDTO allListDTO = new AllListDTO(List.of(
                new ListDTO(1, "To Do"),
                new ListDTO(2, "Completed"),
                new ListDTO(3, "Deployed")
        ));
        when(listService.fetchAllFor(boardId)).thenReturn(allListDTO);

        mockMvc.perform(get("/api/lists")
                        .queryParam("boardId", String.valueOf(boardId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lists", hasSize(3)))
                .andExpect(jsonPath("$.lists[*].id", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$.lists[*].title", containsInAnyOrder("To Do", "Completed", "Deployed")));
    }

    @Test
    void shouldUpdateListTitle() throws Exception {
        when(listService.update(new UpdateListCommand(1, "New List title"))).thenReturn(new ListDTO(1, "New List title"));

        mockMvc.perform(put("/api/lists/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "New List title"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("New List title")));
    }
}

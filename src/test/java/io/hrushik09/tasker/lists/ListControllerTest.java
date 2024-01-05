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
        when(listService.create(new CreateListCommand("To Do", 1))).thenReturn(new CreateListResponse(1, "To Do"));

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
        AllListDetailsDTO allListDetailsDTO = new AllListDetailsDTO(List.of(
                new ListDetailsDTO(1, "To Do"),
                new ListDetailsDTO(2, "Completed"),
                new ListDetailsDTO(3, "Deployed")
        ));
        when(listService.fetchAllFor(boardId)).thenReturn(allListDetailsDTO);

        mockMvc.perform(get("/api/lists")
                        .queryParam("boardId", String.valueOf(boardId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lists", hasSize(3)))
                .andExpect(jsonPath("$.lists[*].id", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$.lists[*].title", containsInAnyOrder("To Do", "Completed", "Deployed")));
    }

    @Test
    void shouldUpdateListTitleSuccessfully() throws Exception {
        when(listService.update(new UpdateListCommand(1, "New List title"))).thenReturn(new UpdateListResponse(1, "New List title"));

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

    @Test
    void shouldThrowWhenUpdatingTitleForNonExistingList() throws Exception {
        Integer nonExistingId = 100;
        when(listService.update(new UpdateListCommand(nonExistingId, "Not important"))).thenThrow(new ListDoesNotExistException(nonExistingId));

        mockMvc.perform(put("/api/lists/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "Not important"
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("List with id=" + nonExistingId + " does not exist")));
    }
}

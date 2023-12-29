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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                        .content("""
                                {
                                "title": "To Do",
                                "userId": 1
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("To Do")));
    }

    @Test
    void shouldFetchAllListsForGivenUser() throws Exception {
        Integer userId = 1;
        AllListDTO allListDTO = new AllListDTO(List.of(
                new ListDTO(1, "To Do"),
                new ListDTO(2, "Completed"),
                new ListDTO(3, "Deployed")
        ));
        when(listService.fetchAllFor(userId)).thenReturn(allListDTO);

        mockMvc.perform(get("/api/lists?userId={userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lists", hasSize(3)))
                .andExpect(jsonPath("$.lists[*].id", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$.lists[*].title", containsInAnyOrder("To Do", "Completed", "Deployed")));
    }
}

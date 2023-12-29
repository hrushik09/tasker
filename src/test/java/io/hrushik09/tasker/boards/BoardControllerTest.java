package io.hrushik09.tasker.boards;

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
                        .queryParam("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
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
}

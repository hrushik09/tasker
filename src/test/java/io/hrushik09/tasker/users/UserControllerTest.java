package io.hrushik09.tasker.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        when(userService.create(new CreateUserCommand("user 1"))).thenReturn(new UserDTO(1, "user 1", null, null));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "user 1"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("user 1")));
    }

    @Test
    void shouldFindUserSuccessfully() throws Exception {
        when(userService.findDTOById(1)).thenReturn(new UserDTO(1, "user 2", Instant.now(), Instant.now()));

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("user 2")))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }

    @Test
    void shouldThrowWhenFindingNonExistingUser() throws Exception {
        Integer nonExistingId = 100;
        when(userService.findDTOById(nonExistingId)).thenThrow(new UserDoesNotExistException(nonExistingId));

        mockMvc.perform(get("/api/users/{id}", nonExistingId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("User with id=" + nonExistingId + " does not exist")));
    }
}

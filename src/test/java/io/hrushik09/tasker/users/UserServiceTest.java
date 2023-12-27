package io.hrushik09.tasker.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setId(1);
        user.setName("user 1");
        when(userRepository.save(any())).thenReturn(user);

        UserDTO userDTO = userService.create(new CreateUserCommand("user 1"));

        assertThat(userDTO.id()).isNotNull();
        assertThat(userDTO.name()).isEqualTo("user 1");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User captorValue = userArgumentCaptor.getValue();
        assertThat(captorValue.getName()).isEqualTo("user 1");
    }
}
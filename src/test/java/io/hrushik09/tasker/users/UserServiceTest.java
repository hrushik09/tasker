package io.hrushik09.tasker.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        when(userRepository.save(any())).thenReturn(aUser().withName("user 1").build());

        UserDTO userDTO = userService.create(new CreateUserCommand("user 1"));

        assertThat(userDTO.id()).isNotNull();
        assertThat(userDTO.name()).isEqualTo("user 1");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User captorValue = userArgumentCaptor.getValue();
        assertThat(captorValue.getName()).isEqualTo("user 1");
    }

    @Test
    void shouldGetUserSuccessfully() {
        Optional<User> optional = Optional.of(aUser().withId(1).withName("user 2").build());
        when(userRepository.findById(1)).thenReturn(optional);

        UserDTO userDTO = userService.getById(1);

        assertThat(userDTO.id()).isEqualTo(1);
        assertThat(userDTO.name()).isEqualTo("user 2");
        assertThat(userDTO.createdAt()).isNotNull();
        assertThat(userDTO.updatedAt()).isNotNull();
    }

    @Test
    void shouldThrowWhenGettingNonExistingUser() {
        int nonExistingId = 100;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(nonExistingId))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessage("User with id=" + nonExistingId + " does not exist");
    }
}
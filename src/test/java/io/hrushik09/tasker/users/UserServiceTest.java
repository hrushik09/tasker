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
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String name = "user 1";
        Integer id = 1;
        when(userRepository.save(any())).thenReturn(aUser().withId(id).withName(name).build());

        UserDTO created = userService.create(new CreateUserCommand(name));

        assertThat(created.id()).isEqualTo(id);
        assertThat(created.name()).isEqualTo(name);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User captorValue = userArgumentCaptor.getValue();
        assertThat(captorValue.getName()).isEqualTo(name);
    }

    @Test
    void shouldThrowWhenFindingNonExistingUser() {
        Integer nonExistingId = 100;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findDTOById(nonExistingId))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessage("User with id=" + nonExistingId + " does not exist");
    }

    @Test
    void shouldFindUserSuccessfully() {
        String name = "user 2";
        int id = 1;
        Optional<User> optional = Optional.of(aUser().withId(id).withName(name).build());
        when(userRepository.findById(id)).thenReturn(optional);

        UserDTO fetched = userService.findDTOById(id);

        assertThat(fetched.id()).isEqualTo(id);
        assertThat(fetched.name()).isEqualTo(name);
        assertThat(fetched.createdAt()).isNotNull();
        assertThat(fetched.updatedAt()).isNotNull();
    }
}
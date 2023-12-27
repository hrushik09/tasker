package io.hrushik09.tasker.users;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO create(CreateUserCommand cmd) {
        User user = new User();
        user.setName(cmd.name());
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getName());
    }
}

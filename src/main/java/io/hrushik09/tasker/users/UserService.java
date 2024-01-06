package io.hrushik09.tasker.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResponse create(CreateUserCommand cmd) {
        User user = new User();
        user.setName(cmd.name());
        User savedUser = userRepository.save(user);
        return CreateUserResponse.from(savedUser);
    }

    public UserDetailsDTO fetchDTOById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException(id));
        return UserDetailsDTO.from(user);
    }

    public User getReferenceById(Integer id) {
        return userRepository.getReferenceById(id);
    }
}

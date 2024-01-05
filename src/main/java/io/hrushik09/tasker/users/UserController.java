package io.hrushik09.tasker.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateUserResponse create(@RequestBody @Valid CreateUserRequest request) {
        CreateUserCommand cmd = new CreateUserCommand(request.name());
        return userService.create(cmd);
    }

    @GetMapping("/{id}")
    UserDetailsDTO findById(@PathVariable Integer id) {
        return userService.findDTOById(id);
    }
}

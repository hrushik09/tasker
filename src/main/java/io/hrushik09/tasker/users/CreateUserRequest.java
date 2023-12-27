package io.hrushik09.tasker.users;

import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequest(
        @NotEmpty(message = "name should be non-empty")
        String name
) {
}

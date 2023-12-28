package io.hrushik09.tasker.users;

import java.time.Instant;

public record UserDTO(
        int id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
    static UserDTO from(User savedUser) {
        return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getCreatedAt(), savedUser.getUpdatedAt());
    }
}

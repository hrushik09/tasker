package io.hrushik09.tasker.users;

import java.time.Instant;

public record UserDTO(
        int id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
    static UserDTO from(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getCreatedAt(), user.getUpdatedAt());
    }
}

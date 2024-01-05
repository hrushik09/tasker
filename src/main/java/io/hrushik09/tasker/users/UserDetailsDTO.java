package io.hrushik09.tasker.users;

import java.time.Instant;

public record UserDetailsDTO(
        Integer id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserDetailsDTO from(User user) {
        return new UserDetailsDTO(user.getId(), user.getName(), user.getCreatedAt(), user.getUpdatedAt());
    }
}

package io.hrushik09.tasker.users;

public record CreateUserResponse(
        Integer id,
        String name
) {
    static CreateUserResponse from(User user) {
        return new CreateUserResponse(user.getId(), user.getName());
    }
}

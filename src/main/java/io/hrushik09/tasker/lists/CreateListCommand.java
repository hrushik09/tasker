package io.hrushik09.tasker.lists;

public record CreateListCommand(
        String title,
        int userId
) {
}

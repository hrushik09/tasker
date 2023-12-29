package io.hrushik09.tasker.lists;

public record UpdateListCommand(
        Integer id,
        String title
) {
}

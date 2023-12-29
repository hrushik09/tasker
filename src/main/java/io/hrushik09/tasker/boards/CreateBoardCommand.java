package io.hrushik09.tasker.boards;

public record CreateBoardCommand(
        String title,
        Integer userId
) {
}

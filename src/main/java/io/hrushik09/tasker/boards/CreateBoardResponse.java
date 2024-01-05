package io.hrushik09.tasker.boards;

public record CreateBoardResponse(
        Integer id,
        String title
) {
    public static CreateBoardResponse from(Board board) {
        return new CreateBoardResponse(board.getId(), board.getTitle());
    }
}

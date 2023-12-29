package io.hrushik09.tasker.boards;

public record BoardDTO(
        int id,
        String title
) {
    public static BoardDTO from(Board board) {
        return new BoardDTO(board.getId(), board.getTitle());
    }
}

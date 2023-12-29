package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;

    public BoardService(BoardRepository boardRepository, UserService userService) {
        this.boardRepository = boardRepository;
        this.userService = userService;
    }

    @Transactional
    public BoardDTO create(CreateBoardCommand cmd) {
        Board board = new Board();
        board.setTitle(cmd.title());
        board.setUser(userService.getReferenceById(cmd.userId()));
        Board savedBoard = boardRepository.save(board);
        return BoardDTO.from(savedBoard);
    }
}

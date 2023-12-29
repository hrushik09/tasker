package io.hrushik09.tasker.boards;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDTO create(@RequestParam Integer userId, @RequestBody @Valid CreateBoardRequest request) {
        return boardService.create(new CreateBoardCommand(request.title(), userId));
    }
}

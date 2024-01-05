package io.hrushik09.tasker.boards;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final BoardDataService boardDataService;

    public BoardController(BoardService boardService, BoardDataService boardDataService) {
        this.boardService = boardService;
        this.boardDataService = boardDataService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateBoardResponse create(@RequestParam Integer userId, @RequestBody @Valid CreateBoardRequest request) {
        return boardService.create(new CreateBoardCommand(request.title(), userId));
    }

    @GetMapping("/{id}")
    BoardDetailsDTO fetchAllData(@PathVariable Integer id) {
        return boardDataService.fetchAllData(new FetchBoardDataQuery(id));
    }
}

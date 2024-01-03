package io.hrushik09.tasker.lists;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lists")
public class ListController {
    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ListDTO create(@RequestParam Integer boardId, @RequestBody @Valid CreateListRequest request) {
        CreateListCommand cmd = new CreateListCommand(request.title(), boardId);
        return listService.create(cmd);
    }

    @GetMapping
    AllListDTO fetchAllFor(@RequestParam Integer boardId) {
        return listService.fetchAllFor(boardId);
    }

    @PutMapping("/{id}")
    ListDTO update(@PathVariable Integer id, @RequestBody @Valid UpdateListRequest request) {
        return listService.update(new UpdateListCommand(id, request.title()));
    }
}

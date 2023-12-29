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
    public ListDTO create(@RequestBody @Valid CreateListRequest request) {
        CreateListCommand cmd = new CreateListCommand(request.title(), request.userId());
        return listService.create(cmd);
    }

    @GetMapping
    public AllListDTO fetchAllFor(@RequestParam(value = "userId") Integer userId) {
        return listService.fetchAllFor(userId);
    }
}

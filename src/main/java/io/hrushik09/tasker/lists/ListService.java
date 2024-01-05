package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.boards.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListService {
    private final ListRepository listRepository;
    private final BoardService boardService;

    public ListService(ListRepository listRepository, BoardService boardService) {
        this.listRepository = listRepository;
        this.boardService = boardService;
    }

    @Transactional
    public CreateListResponse create(CreateListCommand cmd) {
        List list = new List();
        list.setTitle(cmd.title());
        list.setBoard(boardService.getReferenceById(cmd.boardId()));
        List saved = listRepository.save(list);
        return CreateListResponse.from(saved);
    }

    public AllListDetailsDTO fetchAllFor(Integer boardId) {
        return new AllListDetailsDTO(listRepository.fetchAllFor(boardId));
    }

    public UpdateListResponse update(UpdateListCommand cmd) {
        List fetched = listRepository.findById(cmd.id()).orElseThrow(() -> new ListDoesNotExistException(cmd.id()));
        fetched.setTitle(cmd.title());
        List updated = listRepository.save(fetched);
        return UpdateListResponse.from(updated);
    }

    public List getReferenceById(int id) {
        return listRepository.getReferenceById(id);
    }
}

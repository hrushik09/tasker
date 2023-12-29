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
    public ListDTO create(CreateListCommand cmd) {
        List list = new List();
        list.setTitle(cmd.title());
        list.setBoard(boardService.getReferenceById(cmd.boardId()));
        List saved = listRepository.save(list);
        return ListDTO.from(saved);
    }

    public AllListDTO fetchAllFor(Integer boardId) {
        return new AllListDTO(listRepository.fetchAllFor(boardId));
    }

    public ListDTO update(UpdateListCommand cmd) {
        List fetched = listRepository.findById(cmd.id()).orElseThrow(() -> new ListDoesNotExistException(cmd.id()));
        fetched.setTitle(cmd.title());
        List updated = listRepository.save(fetched);
        return ListDTO.from(updated);
    }
}

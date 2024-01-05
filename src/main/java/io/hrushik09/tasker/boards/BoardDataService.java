package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.cards.AllCardMinDetailsDTO;
import io.hrushik09.tasker.cards.CardService;
import io.hrushik09.tasker.lists.AllListDetailsDTO;
import io.hrushik09.tasker.lists.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoardDataService {
    private final BoardRepository boardRepository;
    private final ListService listService;
    private final CardService cardService;

    public BoardDataService(BoardRepository boardRepository, ListService listService, CardService cardService) {
        this.boardRepository = boardRepository;
        this.listService = listService;
        this.cardService = cardService;
    }

    public BoardDetailsDTO fetchAllData(FetchBoardDataQuery query) {
        boardRepository.findById(query.id()).orElseThrow(() -> new BoardDoesNotExistException(query.id()));
        AllListDetailsDTO allListDetailsDTO = listService.fetchAllFor(query.id());
        AllCardMinDetailsDTO allCardMinDetailsDTO = cardService.fetchAllFor(query.id());
        return new BoardDetailsDTO(query.id(), allListDetailsDTO.lists(), allCardMinDetailsDTO.cards());
    }
}

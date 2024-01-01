package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.cards.AllCardMinDTO;
import io.hrushik09.tasker.cards.CardMinDTO;
import io.hrushik09.tasker.cards.CardService;
import io.hrushik09.tasker.lists.AllListDTO;
import io.hrushik09.tasker.lists.ListDTO;
import io.hrushik09.tasker.lists.ListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardDataServiceTest {
    private BoardDataService boardDataService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ListService listService;
    @Mock
    private CardService cardService;

    @BeforeEach
    void setUp() {
        boardDataService = new BoardDataService(boardRepository, listService, cardService);
    }

    @Test
    void shouldThrowWhenFetchingAllDataForNonExistingBoardId() {
        Integer nonExistingBoardId = 1;
        assertThatThrownBy(() -> boardDataService.fetchAllData(new FetchBoardDataQuery(nonExistingBoardId)))
                .isInstanceOf(BoardDoesNotExistException.class)
                .hasMessage("Board with id=" + nonExistingBoardId + " does not exist");
    }

    @Test
    void shouldFetchAllDataForGivenBoardSuccessfully() {
        Integer boardId = 1;
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(aBoard().withId(1).build()));
        List<ListDTO> lists = List.of(
                new ListDTO(1, "To Do"),
                new ListDTO(2, "Completed"),
                new ListDTO(3, "Deployed")
        );
        when(listService.fetchAllFor(boardId)).thenReturn(new AllListDTO(lists));
        List<CardMinDTO> cards = List.of(
                new CardMinDTO(1, "Card 1", 1),
                new CardMinDTO(2, "Card 2", 2),
                new CardMinDTO(3, "Card 3", 1),
                new CardMinDTO(4, "Card 4", 3),
                new CardMinDTO(5, "Card 5", 1)
        );
        when(cardService.fetchAllFor(boardId)).thenReturn(new AllCardMinDTO(cards));

        BoardDataDTO boardDataDTO = boardDataService.fetchAllData(new FetchBoardDataQuery(boardId));

        assertThat(boardDataDTO.id()).isEqualTo(boardId);
        assertThat(boardDataDTO.lists()).hasSize(3);
        assertThat(boardDataDTO.lists()).extracting("id").containsExactlyInAnyOrder(1, 2, 3);
        assertThat(boardDataDTO.lists()).extracting("title").containsExactlyInAnyOrder("To Do", "Completed", "Deployed");
        assertThat(boardDataDTO.cards()).hasSize(5);
        assertThat(boardDataDTO.cards()).extracting("id").containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        assertThat(boardDataDTO.cards()).extracting("title").containsExactlyInAnyOrder("Card 1", "Card 2", "Card 3", "Card 4", "Card 5");
        assertThat(boardDataDTO.cards()).extracting("listId").containsExactlyInAnyOrder(1, 2, 1, 3, 1);
    }
}
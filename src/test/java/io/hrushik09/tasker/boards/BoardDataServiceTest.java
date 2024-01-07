package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.cards.AllCardMinDetailsDTO;
import io.hrushik09.tasker.cards.CardMinDetailsDTO;
import io.hrushik09.tasker.cards.CardService;
import io.hrushik09.tasker.lists.AllListDetailsDTO;
import io.hrushik09.tasker.lists.ListDetailsDTO;
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
        when(boardRepository.findById(boardId))
                .thenReturn(Optional.of(aBoard().withId(boardId).build()));
        List<ListDetailsDTO> lists = List.of(
                new ListDetailsDTO(1, "To Do"),
                new ListDetailsDTO(2, "Completed"),
                new ListDetailsDTO(3, "Deployed")
        );
        when(listService.fetchAllFor(boardId)).thenReturn(new AllListDetailsDTO(lists));
        List<CardMinDetailsDTO> cards = List.of(
                new CardMinDetailsDTO(1, "Card 1", 1),
                new CardMinDetailsDTO(2, "Card 2", 2),
                new CardMinDetailsDTO(3, "Card 3", 1),
                new CardMinDetailsDTO(4, "Card 4", 3),
                new CardMinDetailsDTO(5, "Card 5", 1)
        );
        when(cardService.fetchAllFor(boardId)).thenReturn(new AllCardMinDetailsDTO(cards));

        BoardDetailsDTO boardDetailsDTO = boardDataService.fetchAllData(new FetchBoardDataQuery(boardId));

        assertThat(boardDetailsDTO.id()).isEqualTo(boardId);
        assertThat(boardDetailsDTO.lists()).hasSize(3);
        assertThat(boardDetailsDTO.lists()).extracting("id")
                .containsExactly(1, 2, 3);
        assertThat(boardDetailsDTO.lists()).extracting("title")
                .containsExactly("To Do", "Completed", "Deployed");
        assertThat(boardDetailsDTO.cards()).hasSize(5);
        assertThat(boardDetailsDTO.cards()).extracting("id")
                .containsExactly(1, 2, 3, 4, 5);
        assertThat(boardDetailsDTO.cards()).extracting("title")
                .containsExactly("Card 1", "Card 2", "Card 3", "Card 4", "Card 5");
        assertThat(boardDetailsDTO.cards()).extracting("listId")
                .containsExactly(1, 2, 1, 3, 1);
    }
}
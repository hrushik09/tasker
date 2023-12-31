package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.boards.BoardBuilder;
import io.hrushik09.tasker.boards.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {
    @Mock
    private ListRepository listRepository;
    @Mock
    private BoardService boardService;
    private ListService listService;

    @BeforeEach
    void setUp() {
        listService = new ListService(listRepository, boardService);
    }

    @Test
    void shouldCreateListSuccessfully() {
        Integer boardId = 1;
        BoardBuilder boardBuilder = aBoard().withId(boardId);
        when(boardService.getReferenceById(boardId)).thenReturn(boardBuilder.build());
        Integer listId = 1;
        String title = "To Do";
        List list = aList().withId(listId)
                .withTitle(title)
                .with(boardBuilder)
                .build();
        when(listRepository.save(any())).thenReturn(list);

        ListDTO created = listService.create(new CreateListCommand(title, boardId));

        assertThat(created.id()).isEqualTo(listId);
        assertThat(created.title()).isEqualTo(title);
        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(listRepository).save(listArgumentCaptor.capture());
        List captorValue = listArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo(title);
        assertThat(captorValue.getBoard().getId()).isEqualTo(boardId);
    }

    @Test
    void shouldFetchAllListsForGivenBoard() {
        Integer boardId = 1;
        java.util.List<ListDTO> dtos = java.util.List.of(
                new ListDTO(1, "To Do"),
                new ListDTO(2, "Completed"),
                new ListDTO(3, "Deployed")
        );
        when(listRepository.fetchAllFor(boardId)).thenReturn(dtos);

        AllListDTO fetched = listService.fetchAllFor(boardId);

        assertThat(fetched.lists()).hasSize(3);
        assertThat(fetched.lists()).extracting("id").containsExactlyInAnyOrder(1, 2, 3);
        assertThat(fetched.lists()).extracting("title").containsExactlyInAnyOrder("To Do", "Completed", "Deployed");
    }

    @Test
    void shouldUpdateListTitleSuccessfully() {
        Optional<List> optional = Optional.of(aList().withId(1).build());
        when(listRepository.findById(1)).thenReturn(optional);
        when(listRepository.save(any())).thenReturn(aList().withId(1).withTitle("Updated title").build());

        ListDTO updated = listService.update(new UpdateListCommand(1, "Updated title"));

        assertThat(updated.id()).isEqualTo(1);
        assertThat(updated.title()).isEqualTo("Updated title");
        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(listRepository).save(listArgumentCaptor.capture());
        List captorValue = listArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo("Updated title");
    }

    @Test
    void shouldThrowWhenUpdatingTitleForNonExistingList() {
        Integer nonExistingId = 100;
        when(listRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listService.update(new UpdateListCommand(nonExistingId, "Not important")))
                .isInstanceOf(ListDoesNotExistException.class)
                .hasMessage("List with id=" + nonExistingId + " does not exist");
    }
}
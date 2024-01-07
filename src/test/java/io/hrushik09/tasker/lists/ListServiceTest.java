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
    private ListService listService;
    @Mock
    private ListRepository listRepository;
    @Mock
    private BoardService boardService;

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
        ListBuilder listBuilder = aList().withId(listId).withTitle(title).with(boardBuilder);
        when(listRepository.save(any())).thenReturn(listBuilder.build());

        CreateListResponse created = listService.create(new CreateListCommand(title, boardId));

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
        java.util.List<ListDetailsDTO> listDetailsDTOS = java.util.List.of(
                new ListDetailsDTO(1, "To Do"),
                new ListDetailsDTO(2, "Completed"),
                new ListDetailsDTO(3, "Deployed")
        );
        when(listRepository.fetchAllFor(boardId)).thenReturn(listDetailsDTOS);

        AllListDetailsDTO fetched = listService.fetchAllFor(boardId);

        assertThat(fetched.lists()).hasSize(3);
        assertThat(fetched.lists()).extracting("id")
                .containsExactlyInAnyOrder(1, 2, 3);
        assertThat(fetched.lists()).extracting("title")
                .containsExactlyInAnyOrder("To Do", "Completed", "Deployed");
    }

    @Test
    void shouldThrowWhenUpdatingTitleForNonExistingList() {
        Integer nonExistingId = 100;
        when(listRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listService.update(new UpdateListCommand(nonExistingId, "Not important")))
                .isInstanceOf(ListDoesNotExistException.class)
                .hasMessage("List with id=" + nonExistingId + " does not exist");
    }

    @Test
    void shouldUpdateListTitleSuccessfully() {
        int id = 1;
        ListBuilder listBuilder = aList().withId(id);
        when(listRepository.findById(id)).thenReturn(Optional.of(listBuilder.build()));
        String updatedTitle = "Updated title";
        when(listRepository.save(any())).thenReturn(listBuilder.but().withTitle(updatedTitle).build());

        UpdateListResponse updated = listService.update(new UpdateListCommand(id, updatedTitle));

        assertThat(updated.id()).isEqualTo(id);
        assertThat(updated.title()).isEqualTo(updatedTitle);
        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(listRepository).save(listArgumentCaptor.capture());
        List captorValue = listArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo(updatedTitle);
    }
}
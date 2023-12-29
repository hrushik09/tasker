package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {
    @Mock
    private ListRepository listRepository;
    @Mock
    private UserService userService;
    private ListService listService;

    @BeforeEach
    void setUp() {
        listService = new ListService(listRepository, userService);
    }

    @Test
    void shouldCreateListSuccessfully() {
        List list = aList().withId(1)
                .withTitle("To Do")
                .with(aUser().withId(1))
                .build();
        when(listRepository.save(any())).thenReturn(list);
        when(userService.getReferenceById(1)).thenReturn(aUser().withId(1).build());

        ListDTO listDTO = listService.create(new CreateListCommand("To Do", 1));

        assertThat(listDTO.id()).isNotNull();
        assertThat(listDTO.title()).isEqualTo("To Do");
        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(listRepository).save(listArgumentCaptor.capture());
        List captorValue = listArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo("To Do");
        assertThat(captorValue.getUser().getId()).isEqualTo(1);
    }

    @Test
    void shouldFetchAllListsForGivenUser() {
        Integer userId = 1;
        java.util.List<ListDTO> dtos = java.util.List.of(
                new ListDTO(1, "To Do"),
                new ListDTO(2, "Completed"),
                new ListDTO(3, "Deployed")
        );
        when(listRepository.fetchAllFor(userId)).thenReturn(dtos);

        AllListDTO fetched = listService.fetchAllFor(userId);

        assertThat(fetched.lists()).hasSize(3);
        assertThat(fetched.lists()).extracting("id").containsExactlyInAnyOrder(1, 2, 3);
        assertThat(fetched.lists()).extracting("title").containsExactlyInAnyOrder("To Do", "Completed", "Deployed");
    }
}
package io.hrushik09.tasker.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {
    @Mock
    private ListRepository listRepository;
    private ListService listService;

    @BeforeEach
    void setUp() {
        listService = new ListService(listRepository);
    }

    @Test
    void shouldCreateListSuccessfully() {
        List list = new List();
        list.setId(1);
        list.setTitle("To Do");
        list.setUserId(1);
        when(listRepository.save(any())).thenReturn(list);

        ListDTO listDTO = listService.create(new CreateListCommand("To Do", 1));

        assertThat(listDTO.id()).isNotNull();
        assertThat(listDTO.title()).isEqualTo("To Do");
        assertThat(listDTO.userId()).isEqualTo(1);
        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(listRepository).save(listArgumentCaptor.capture());
        List captorValue = listArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo("To Do");
        assertThat(captorValue.getUserId()).isEqualTo(1);
    }
}
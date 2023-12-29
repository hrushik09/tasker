package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.users.UserBuilder;
import io.hrushik09.tasker.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserService userService;
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, userService);
    }

    @Test
    void shouldCreateBoardSuccessfully() {
        UserBuilder userBuilder = aUser().withId(2);
        when(userService.getReferenceById(2)).thenReturn(userBuilder.build());
        Board board = new Board();
        board.setId(1);
        board.setTitle("My Board");
        board.setUser(userBuilder.build());
        when(boardRepository.save(any())).thenReturn(board);

        BoardDTO createdBoard = boardService.create(new CreateBoardCommand("My Board", 2));

        assertThat(createdBoard.id()).isNotNull();
        assertThat(createdBoard.title()).isEqualTo("My Board");
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());
        Board captorValue = boardArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo("My Board");
        assertThat(captorValue.getUser().getId()).isEqualTo(2);
    }
}
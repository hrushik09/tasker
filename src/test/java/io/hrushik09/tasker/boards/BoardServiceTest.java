package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.users.UserBuilder;
import io.hrushik09.tasker.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    private BoardService boardService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, userService);
    }

    @Test
    void shouldCreateBoardSuccessfully() {
        Integer userId = 2;
        UserBuilder userBuilder = aUser().withId(userId);
        when(userService.getReferenceById(userId)).thenReturn(userBuilder.build());
        Integer boardId = 1;
        String title = "My Board";
        Board board = aBoard().withId(boardId).withTitle(title).with(userBuilder).build();
        when(boardRepository.save(any())).thenReturn(board);

        BoardDTO created = boardService.create(new CreateBoardCommand(title, userId));

        assertThat(created.id()).isEqualTo(boardId);
        assertThat(created.title()).isEqualTo(title);
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());
        Board captorValue = boardArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo(title);
        assertThat(captorValue.getUser().getId()).isEqualTo(userId);
    }
}
package io.hrushik09.tasker;

import io.hrushik09.tasker.boards.BoardDTO;
import io.hrushik09.tasker.boards.BoardService;
import io.hrushik09.tasker.boards.CreateBoardCommand;
import io.hrushik09.tasker.lists.CreateListCommand;
import io.hrushik09.tasker.lists.ListDTO;
import io.hrushik09.tasker.lists.ListService;
import io.hrushik09.tasker.users.CreateUserCommand;
import io.hrushik09.tasker.users.UserDTO;
import io.hrushik09.tasker.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndToEndTestDataPersister {
    @Autowired
    private UserService userService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private ListService listService;

    public UserDTO havingPersistedUser(String name) {
        return userService.create(new CreateUserCommand(name));
    }

    public UserDTO havingPersistedUser() {
        return userService.create(new CreateUserCommand("Not important"));
    }

    public BoardDTO havingPersistedBoard(Integer userId) {
        return boardService.create(new CreateBoardCommand("Not important", userId));
    }

    public ListDTO havingPersistedList(String title, Integer boardId) {
        return listService.create(new CreateListCommand(title, boardId));
    }
}

package io.hrushik09.tasker;

import io.hrushik09.tasker.boards.BoardService;
import io.hrushik09.tasker.boards.CreateBoardCommand;
import io.hrushik09.tasker.boards.CreateBoardResponse;
import io.hrushik09.tasker.cards.*;
import io.hrushik09.tasker.lists.CreateListCommand;
import io.hrushik09.tasker.lists.CreateListResponse;
import io.hrushik09.tasker.lists.ListService;
import io.hrushik09.tasker.users.CreateUserCommand;
import io.hrushik09.tasker.users.CreateUserResponse;
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
    @Autowired
    private CardService cardService;

    public CreateUserResponse havingPersistedUser(String name) {
        return userService.create(new CreateUserCommand(name));
    }

    public CreateUserResponse havingPersistedUser() {
        return userService.create(new CreateUserCommand("Not important"));
    }

    public CreateBoardResponse havingPersistedBoard() {
        CreateUserResponse createUserResponse = havingPersistedUser();
        return boardService.create(new CreateBoardCommand("Not important", createUserResponse.id()));
    }

    public CreateBoardResponse havingPersistedBoard(Integer userId) {
        return boardService.create(new CreateBoardCommand("Not important", userId));
    }

    public CreateListResponse havingPersistedList(String title, Integer boardId) {
        return listService.create(new CreateListCommand(title, boardId));
    }

    public CreateListResponse havingPersistedList() {
        CreateUserResponse createUserResponse = havingPersistedUser();
        CreateBoardResponse createBoardResponse = havingPersistedBoard(createUserResponse.id());
        return havingPersistedList("Not important", createBoardResponse.id());
    }

    public CreateCardResponse havingPersistedCard(String title, Integer listId) {
        return cardService.create(new CreateCardCommand(listId, title));
    }

    public CreateCardResponse havingPersistedCard() {
        CreateUserResponse createUserResponse = havingPersistedUser();
        CreateBoardResponse createBoardResponse = havingPersistedBoard(createUserResponse.id());
        CreateListResponse createListResponse = havingPersistedList("Not important", createBoardResponse.id());
        return havingPersistedCard("No important", createListResponse.id());
    }

    public UpdateCardDescriptionResponse havingUpdatedCardDescription(Integer id, String description) {
        return cardService.updateDescription(new UpdateDescriptionCommand(id, description));
    }
}

package io.hrushik09.tasker;

import io.hrushik09.tasker.boards.BoardService;
import io.hrushik09.tasker.boards.CreateBoardCommand;
import io.hrushik09.tasker.boards.CreateBoardResponse;
import io.hrushik09.tasker.cards.CardService;
import io.hrushik09.tasker.cards.CreateCardCommand;
import io.hrushik09.tasker.cards.CreateCardResponse;
import io.hrushik09.tasker.cards.UpdateCardCommand;
import io.hrushik09.tasker.lists.CreateListCommand;
import io.hrushik09.tasker.lists.CreateListResponse;
import io.hrushik09.tasker.lists.ListService;
import io.hrushik09.tasker.users.CreateUserCommand;
import io.hrushik09.tasker.users.CreateUserResponse;
import io.hrushik09.tasker.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    public CreateUserResponse persistedUser(String name) {
        return userService.create(new CreateUserCommand(name));
    }

    public CreateUserResponse persistedUser() {
        return userService.create(new CreateUserCommand("Not important"));
    }

    public CreateBoardResponse persistedBoard() {
        CreateUserResponse createUserResponse = persistedUser();
        return boardService.create(new CreateBoardCommand("Not important", createUserResponse.id()));
    }

    public CreateBoardResponse persistedBoard(Integer userId) {
        return boardService.create(new CreateBoardCommand("Not important", userId));
    }

    public CreateListResponse persistedList(String title, Integer boardId) {
        return listService.create(new CreateListCommand(title, boardId));
    }

    public CreateListResponse persistedList() {
        CreateUserResponse createUserResponse = persistedUser();
        CreateBoardResponse createBoardResponse = persistedBoard(createUserResponse.id());
        return persistedList("Not important", createBoardResponse.id());
    }

    public CreateCardResponse persistedCard(String title, Integer listId) {
        return cardService.create(new CreateCardCommand(listId, title));
    }

    public CreateCardResponse persistedCard() {
        CreateUserResponse createUserResponse = persistedUser();
        CreateBoardResponse createBoardResponse = persistedBoard(createUserResponse.id());
        CreateListResponse createListResponse = persistedList("Not important", createBoardResponse.id());
        return persistedCard("Not important", createListResponse.id());
    }

    public void updatedCard(Integer id, Map<String, Object> fields) {
        cardService.update(new UpdateCardCommand(id, fields));
    }
}

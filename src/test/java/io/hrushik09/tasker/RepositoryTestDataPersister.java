package io.hrushik09.tasker;

import io.hrushik09.tasker.boards.Board;
import io.hrushik09.tasker.lists.List;
import io.hrushik09.tasker.users.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class RepositoryTestDataPersister {
    public User havingPersistedUser(TestEntityManager entityManager, String name) {
        User user = new User();
        user.setName(name);
        return entityManager.persist(user);
    }

    public Board havingPersistedBoard(TestEntityManager entityManager, String title, User user) {
        Board board = new Board();
        board.setTitle(title);
        board.setUser(user);
        return entityManager.persist(board);
    }

    public List havingPersistedList(TestEntityManager entityManager, String title, Board board) {
        List list = new List();
        list.setTitle(title);
        list.setBoard(board);
        return entityManager.persist(list);
    }
}

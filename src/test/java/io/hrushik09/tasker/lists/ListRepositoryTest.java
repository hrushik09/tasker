package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.RepositoryTest;
import io.hrushik09.tasker.boards.Board;
import io.hrushik09.tasker.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ListRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ListRepository listRepository;

    private User havingPersistedUser(String name) {
        User user = new User();
        user.setName(name);
        return entityManager.persist(user);
    }

    private Board havingPersistedBoard(String title, User user) {
        Board board = new Board();
        board.setTitle(title);
        board.setUser(user);
        return entityManager.persist(board);
    }

    private List havingPersistedList(String title, Board board) {
        List list = new List();
        list.setTitle(title);
        list.setBoard(board);
        return entityManager.persist(list);
    }

    @Test
    void validateFetchForAll() {
        User user = havingPersistedUser("user 1");
        Board board = havingPersistedBoard("Board 1", user);
        List todo = havingPersistedList("To Do", board);
        List completed = havingPersistedList("Completed", board);
        List deployed = havingPersistedList("Deployed", board);
        List futureWorks = havingPersistedList("Future Works", board);
        Board extraBoard = havingPersistedBoard("Extra Board", user);
        List extraList = havingPersistedList("Not important", extraBoard);
        entityManager.flush();
        entityManager.clear();

        java.util.List<ListDTO> fetched = listRepository.fetchAllFor(board.getId());

        assertThat(fetched).hasSize(4);
        assertThat(fetched).extracting("id")
                .containsExactlyInAnyOrder(todo.getId(), completed.getId(), deployed.getId(), futureWorks.getId());
        assertThat(fetched).extracting("title")
                .containsExactlyInAnyOrder("To Do", "Completed", "Deployed", "Future Works");
    }
}
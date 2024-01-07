package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.RepositoryTest;
import io.hrushik09.tasker.RepositoryTestDataPersister;
import io.hrushik09.tasker.boards.Board;
import io.hrushik09.tasker.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ListRepositoryTest {
    private final RepositoryTestDataPersister having = new RepositoryTestDataPersister();
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ListRepository listRepository;

    @Test
    void validateFetchForAll() {
        User user = having.persistedUser(entityManager, "user 1");
        Board board = having.persistedBoard(entityManager, "Board 1", user);
        List todo = having.persistedList(entityManager, "To Do", board);
        List completed = having.persistedList(entityManager, "Completed", board);
        List deployed = having.persistedList(entityManager, "Deployed", board);
        List futureWorks = having.persistedList(entityManager, "Future Works", board);
        Board extraBoard = having.persistedBoard(entityManager, "Extra Board", user);
        List extraList = having.persistedList(entityManager, "Not important", extraBoard);
        entityManager.flush();
        entityManager.clear();

        java.util.List<ListDetailsDTO> fetched = listRepository.fetchAllFor(board.getId());

        assertThat(fetched).hasSize(4);
        assertThat(fetched).extracting("id")
                .containsExactlyInAnyOrder(todo.getId(), completed.getId(), deployed.getId(), futureWorks.getId());
        assertThat(fetched).extracting("title")
                .containsExactlyInAnyOrder(todo.getTitle(), completed.getTitle(), deployed.getTitle(), futureWorks.getTitle());
    }
}
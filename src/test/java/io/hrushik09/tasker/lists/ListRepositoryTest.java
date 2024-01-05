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
    private final RepositoryTestDataPersister dataPersister = new RepositoryTestDataPersister();
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ListRepository listRepository;

    @Test
    void validateFetchForAll() {
        User user = dataPersister.havingPersistedUser(entityManager, "user 1");
        Board board = dataPersister.havingPersistedBoard(entityManager, "Board 1", user);
        List todo = dataPersister.havingPersistedList(entityManager, "To Do", board);
        List completed = dataPersister.havingPersistedList(entityManager, "Completed", board);
        List deployed = dataPersister.havingPersistedList(entityManager, "Deployed", board);
        List futureWorks = dataPersister.havingPersistedList(entityManager, "Future Works", board);
        Board extraBoard = dataPersister.havingPersistedBoard(entityManager, "Extra Board", user);
        List extraList = dataPersister.havingPersistedList(entityManager, "Not important", extraBoard);
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
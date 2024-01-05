package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.RepositoryTest;
import io.hrushik09.tasker.RepositoryTestDataPersister;
import io.hrushik09.tasker.boards.Board;
import io.hrushik09.tasker.lists.List;
import io.hrushik09.tasker.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class CardRepositoryTest {
    private final RepositoryTestDataPersister dataPersister = new RepositoryTestDataPersister();
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CardRepository cardRepository;

    @Test
    void validateFetchForAll() {
        User user = dataPersister.havingPersistedUser(entityManager, "user 1");
        Board board = dataPersister.havingPersistedBoard(entityManager, "Board 1", user);
        List todo = dataPersister.havingPersistedList(entityManager, "To Do", board);
        List completed = dataPersister.havingPersistedList(entityManager, "Completed", board);
        List deployed = dataPersister.havingPersistedList(entityManager, "Deployed", board);
        List futureWorks = dataPersister.havingPersistedList(entityManager, "Future Works", board);
        Card card1 = dataPersister.havingPersistedCard(entityManager, "Card 1", todo);
        Card card2 = dataPersister.havingPersistedCard(entityManager, "Card 2", completed);
        Card card3 = dataPersister.havingPersistedCard(entityManager, "Card 3", futureWorks);
        Card card4 = dataPersister.havingPersistedCard(entityManager, "Card 4", deployed);
        Card card5 = dataPersister.havingPersistedCard(entityManager, "Card 5", completed);
        Card card6 = dataPersister.havingPersistedCard(entityManager, "Card 6", todo);

        Board extraBoard = dataPersister.havingPersistedBoard(entityManager, "Extra Board", user);
        List extraList = dataPersister.havingPersistedList(entityManager, "Extra List", extraBoard);
        Card extraCard = dataPersister.havingPersistedCard(entityManager, "Extra Card", extraList);

        java.util.List<CardMinDetailsDTO> cards = cardRepository.fetchForAll(board.getId());

        assertThat(cards).hasSize(6);
        assertThat(cards).extracting("id").containsExactlyInAnyOrder(card1.getId(), card2.getId(), card3.getId(), card4.getId(), card5.getId(), card6.getId());
        assertThat(cards).extracting("title").containsExactlyInAnyOrder(card1.getTitle(), card2.getTitle(), card3.getTitle(), card4.getTitle(), card5.getTitle(), card6.getTitle());
        assertThat(cards).extracting("listId").containsExactlyInAnyOrder(card1.getList().getId(), card2.getList().getId(), card3.getList().getId(), card4.getList().getId(), card5.getList().getId(), card6.getList().getId());
    }
}
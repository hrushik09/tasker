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
    private final RepositoryTestDataPersister having = new RepositoryTestDataPersister();
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CardRepository cardRepository;

    @Test
    void shouldValidateCardDetails() {
        User user = having.persistedUser(entityManager, "user 1");
        Board board = having.persistedBoard(entityManager, "Board 1", user);
        List todo = having.persistedList(entityManager, "To Do", board);
        List completed = having.persistedList(entityManager, "Completed", board);
        List deployed = having.persistedList(entityManager, "Deployed", board);
        List futureWorks = having.persistedList(entityManager, "Future Works", board);
        Card card1 = having.persistedCard(entityManager, "Card 1", todo);
        Card card2 = having.persistedCard(entityManager, "Card 2", completed);
        Card card3 = having.persistedCard(entityManager, "Card 3", futureWorks);
        Card card4 = having.persistedCard(entityManager, "Card 4", deployed);
        Card card5 = having.persistedCard(entityManager, "Card 5", completed);
        Card card6 = having.persistedCard(entityManager, "Card 6", todo);
        Board extraBoard = having.persistedBoard(entityManager, "Extra Board", user);
        List extraList = having.persistedList(entityManager, "Extra List", extraBoard);
        Card extraCard = having.persistedCard(entityManager, "Extra Card", extraList);
        entityManager.flush();
        entityManager.clear();

        java.util.List<CardMinDetailsDTO> cards = cardRepository.fetchForAll(board.getId());

        assertThat(cards).hasSize(6);
        assertThat(cards).extracting("id").containsExactly(card1.getId(), card2.getId(), card3.getId(), card4.getId(), card5.getId(), card6.getId());
        assertThat(cards).extracting("title").containsExactly(card1.getTitle(), card2.getTitle(), card3.getTitle(), card4.getTitle(), card5.getTitle(), card6.getTitle());
        assertThat(cards).extracting("listId").containsExactly(card1.getList().getId(), card2.getList().getId(), card3.getList().getId(), card4.getList().getId(), card5.getList().getId(), card6.getList().getId());
    }

    @Test
    void shouldNotFetchUnarchivedCards() {
        User user = having.persistedUser(entityManager, "user 1");
        Board board = having.persistedBoard(entityManager, "Board 1", user);
        List todo = having.persistedList(entityManager, "To Do", board);
        Card card1 = having.persistedCard(entityManager, "Card 1", todo);
        Card card2 = having.persistedCard(entityManager, "Card 2", todo);
        Card card3 = having.persistedCard(entityManager, "Card 3", todo);
        having.archivedCard(entityManager, card2);
        entityManager.flush();
        entityManager.clear();

        java.util.List<CardMinDetailsDTO> cards = cardRepository.fetchForAll(board.getId());

        assertThat(cards).hasSize(2);
        assertThat(cards).extracting("id").containsExactly(card1.getId(), card3.getId());
    }
}